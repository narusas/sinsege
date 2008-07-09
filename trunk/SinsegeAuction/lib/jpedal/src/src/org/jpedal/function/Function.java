/*
 * Created on 31-Dec-2003
 *
 * Based on Tom Phelps code
 */
package org.jpedal.function;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.jpedal.io.PdfObjectReader;
import org.jpedal.utils.Strip;

/**
 * @author markee
 *
 *Provides functions
 */
public abstract class Function implements Serializable {
	//<start-13>
	/**
     //<end-13>
     }
     //<start-13>
	 */
	protected Function(){}

	public abstract void compute(float[] input, float[] output,float[] domain);

	public abstract String[] compute(float[] values,float[] domain);

	/** Returns output arity. */
	public int getN() { return range.length / 2; }

	/**
     Returns function instance
	 */
	public static Function getInstance(byte[] stream,Map tintTransform,float[] domain,float[] range,int functionType,PdfObjectReader currentPdfFile) {


		Function fn=null;

		switch (functionType) {
		case 0 :
			fn=new Sampled(stream,tintTransform,domain,range,currentPdfFile);
			break;
		case 2 :
			fn=new Exponential(tintTransform,domain,range,currentPdfFile);
			break;
		case 3 :
			fn = new Stitching(tintTransform,domain,range,currentPdfFile);
			break;
		case 4 :
			fn=new Calculator(stream,domain,range);
			break;
		}

		return fn;
	}

	float[] range= null;    // required for type 0 and 4 only

	/**
     Process common dictionary elements domain and range.
	 */
	Function(float[] domain,float[] range)  {

		this.range=range;

	}

	/** Clip <var>in</var> values against <var>clip</var> array. */
	void clip(float[] in, float[] clip) {
		int clipcomp = clip.length / 2;

		for (int i=0; i<clipcomp; i++) {

			float inval=in[i];
			float min=clip[i*2];
			float max=clip[i*2+1];
			if (inval<min) in[i]=min; else if (inval>max) in[i]=max;
		}
	}
}


//e.g., /FunctionType 2 /Domain [ 0 1 ] /Range [ 0 1 0 1 0 1 ] /C0 [ 1 0.976 0.961 ] /C1 [ 1 0.965 0.94901 ] /N 1
class Exponential extends  Function {
	static final float[] C0_DEFAULT = { 0f }, C1_DEFAULT = { 1f };    // Java won't allow static in nested classes

	float[] C0, C1,domain,range;
	float N;

	Exponential(Map tintTransform,float[] domain,float[] range,PdfObjectReader currentPdfFile)  {

		//set order
		String value=(String) tintTransform.get("N");
		if (value != null)
			N=Float.parseFloat(value);

		//set decode
		value=(String) tintTransform.get("C0");
		if (value != null) {
			value=currentPdfFile.getValue(value);

			StringTokenizer matrixValues =new StringTokenizer(value, "[] ");
			C0=new float[matrixValues.countTokens()];
			int i = 0;
			while (matrixValues.hasMoreTokens()) {
				C0[i] = Float.parseFloat(matrixValues.nextToken());
				i++;
			}
		}else{
			C0=new float[1];
			C0[0]=0.0f;
		}

		value=(String) tintTransform.get("C1");
		if (value != null) {
			value=currentPdfFile.getValue(value);

			StringTokenizer matrixValues =new StringTokenizer(value, "[] ");
			C1=new float[matrixValues.countTokens()];
			int i = 0;
			while (matrixValues.hasMoreTokens()) {
				C1[i] = Float.parseFloat(matrixValues.nextToken());
				i++;
			}
		}else{
			C1=new float[1];
			C1[0]=1.0f;
		}

		this.domain=domain;
		this.range=range;
	}

	public int getN() { return C0.length; }  // /Range not required

	/**type 2 function*/
	final public String[] compute(float[] input,float[] domain){

		if(domain==null)
			domain=this.domain;

		int returnValues=C0.length;

		float[] output=new float[returnValues];
		String[] result=new String[returnValues];

		try{

			compute(input,output,domain);
			for(int j=0;j<returnValues;j++){
				result[j]=""+output[returnValues-j-1];
			}

		}catch(Exception e){
			System.out.println("Exp error "+e);
			e.printStackTrace();
		}

		return result;
	}

	public void compute(float[] input, float[] output,float[] domain) {
		clip(input, domain);


		float x=input[0];   // m<=len because PDF.java uses common array for all inputs

		int n = getN();

		//float[] C0=C0, C1=this.C1;
		if (N==1f) for (int i=0; i<n; i++) output[i] = C0[i] + x * (C1[i]-C0[i]);     // special case
		else for (int i=0; i<n; i++) output[i] = C0[i] + (float)Math.pow(x, N) * (C1[i]-C0[i]);

		if (range!=null) clip(output, range);
	}
}


/**
 Process subset of PostScript language, with stack and conditionals.
 Some day could dynamically write new class bytecode -- which is also stack based, and Java VM would optimize!
 LATER: just pass stream to PostScript.java!
 */
class Calculator extends Function{
	//<start-13>
	static final Integer FALSE=new Integer(0), TRUE=new Integer(1);
	static final Character PROC_OPEN=new Character('{'), PROC_CLOSE=new Character('}');
	static final Map op2code;

	static {

		String[] cmds =
			("abs add atan ceiling cos cvi cvr div exp floor idiv ln log mod mul neg sin sqrt sub round truncate"
					+" and bitshift eq false ge gt le lt ne not or true xor"
					+" if ifelse"
					+" copy exch pop dup index roll").split("\\s+");
		// also '{' '}' numbers

		int len = cmds.length;
		op2code = new HashMap(len * 2);
		for (int i=0,imax=len; i<imax; i++) op2code.put(cmds[i], new Integer(i));
	}

	class Op {
		public String op;
		public int code;

		public Object[] iftrue;
		public Object[] iffalse;
		public Op(String op, int code) { this.op=op; this.code=code; }
		public String toString() { return op+"/"+code; }
	}


	Object[] cmds_;   // Number and Op

	private float[] domain,range;

	Calculator(byte[] stream,float[] domain,float[] range) {

		this.range=range;
		this.domain=domain;

		CustomByteArrayInputStream in = new CustomByteArrayInputStream(stream);

		try{
			readToken(in); // assert '{'==xxx   // eat opening '{'
			cmds_ = parse(in);
		}catch(Exception e){
			System.out.println("Exception "+e+" with Calculator function");
		}
	}

	/**type 4 function*/
	final public String[] compute(float[] input,float[] domain){

		if(domain==null)
			domain=this.domain;
		
		int returnValues=range.length/2;
		
		float[] output=new float[returnValues];
		String[] result=new String[returnValues];

		//try{

		compute(input,output,domain);
		
		for(int j=0;j<returnValues;j++)
			result[j]=""+output[returnValues-j-1];
		
		//}catch(Exception e){
		//	System.out.println("Calculator exception "+e);
		//  e.printStackTrace();
		// System.exit(1);
		//}

		return result;
	}

	public void compute(float[] input, float[] output,float[] domain) {

		try{
			clip(input, domain);

			Number[] stk = new Number[100];     // Float and Integer immutable so could potentially make many many instances -- but should be in nursury so create and gc fast... enough?
			int si = 0;     // common pointer

			for (int i=0,imax=domain.length / 2; i<imax; i++) {
				stk[si++] = new Double(input[i]);   // put input on stack
			}

			si = execute(cmds_, stk, si);

			if((domain.length / 2)==1){
				for (int i=0,imax=range.length / 2; i<imax; i++){
					output[i] = (stk[imax-i-1]).floatValue();   // take output from stack
				}
			}else
				for (int i=0,imax=range.length / 2; i<imax; i++){
					output[i] = (stk[i]).floatValue();   // take output from stack
				}
			
			clip(output, range);
			
		}catch(Exception e){
		}

	}


	/**scan through all commands, building a list we can then execute*/
	Object[] parse(CustomByteArrayInputStream in) throws IOException {
		List l = new ArrayList(100);

		while (true) {
			//next value
			Object tok = readToken(in);

			//recursively call, handle if/if else, add to list
			if (tok==PROC_OPEN)
				l.add(parse(in));
			else if (tok==PROC_CLOSE)
				break;
			else if (tok instanceof Number)
				l.add(tok);
			else if (tok instanceof Op) {

				Op op = (Op)tok;

				if (op.code==34){ //if command has set of commands to execute if true
					//op.iftrue = (Op[])l.remove(l.size()-1);        // doudou 07.04.2006 bugfix cominto
					op.iftrue = (Object[])l.remove(l.size()-1);
				}
				else if (op.code==35) { //if else has true and false command set
					op.iffalse = (Op[])l.remove(l.size()-1);
					op.iftrue = (Op[])l.remove(l.size()-1);
				}


				//other command so just add to list
				l.add(op);
			}
		}

		return l.toArray();
	}

	/* @return String, Number, {, }. */
	Object readToken(CustomByteArrayInputStream in)  {
		int c;
		while ((c=in.read())!=-1 && Character.isWhitespace((char)c)) {}

		Object o;
		if (c=='{') o = PROC_OPEN;
		else if (c=='}' || c==-1) o = PROC_CLOSE;
		else if (c>='a' && c<='z') {  // operators all-lowercase
			StringBuffer sb = new StringBuffer(10); sb.append((char)c);
			while ((c=in.read())>='a' && c<='z') sb.append((char)c);
			in.unread(c);
			String op = sb.toString();
			Integer into = (Integer)op2code.get(op);
			o = new Op(op, into.intValue());

		} else {
			StringBuffer sb = new StringBuffer(10); sb.append((char)c);
			boolean ffloat=false;
			while (true) {
				if ((c=in.read())>='0' && c<='9') sb.append((char)c);
				else if (c=='.' /*|| c=='e'*/) ffloat=true;
				else { in.unread(c); break; }
			}
			String s = sb.toString();
			o = (ffloat? (Number)new Double(s): (Number)new Integer(s));
		}

		return o;
	}

	/**
	 * execute list of commands
	 */
	int execute(Object[] cmds, Number[] stk, int si) {

		if (cmds == null)
			return si;  // doudou 07.04.2006 bugfix cominto


		Number tmpEle = null;   // doudou 07.04.2006 bugfix cominto


		for (int i=0,imax=cmds.length,j,n; i<imax; i++) {

			Object o = cmds[i];

			if (o instanceof Number /*|| o instanceof Op[] -- in Op object, not stack*/) { stk[si++] = (Number)o; continue; }

			Op op = (Op)o;
			Number opn = si>0? stk[si-1]: null;     // could open command stream with 'true'

			switch (op.code) {
			// arithmetic
			case 0: stk[si-1] = new Double(Math.abs(opn.doubleValue())); break; // abs
			case 1: stk[si-2] = new Double((stk[si-2]).doubleValue() + opn.doubleValue()); si--; break; // add
			case 2: stk[si-1] = new Double(Math.atan(opn.doubleValue())); break;  // atan
			case 3: stk[si-1] = new Double(Math.ceil(opn.doubleValue())); break;  // ceiling
			case 4: stk[si-1] = new Double(Math.cos(opn.doubleValue())); break; // cos
			case 5: stk[si-1] = new Integer(opn.intValue()); break; // cvi
			case 6: stk[si-1] = new Double(opn.doubleValue()); break; // cvr
			case 7: stk[si-2] = new Double(stk[si-2].doubleValue() / opn.doubleValue()); si--; break; // div
			case 8: stk[si-2] = new Double(Math.pow(stk[si-2].doubleValue(), opn.doubleValue())); si--; break; // exp
			case 9: stk[si-1] = new Double(Math.floor(opn.doubleValue())); break; // floor
			case 10: stk[si-2] = new Integer(stk[si-2].intValue() / opn.intValue()); si--; break; // idiv
			case 11: stk[si-1] = new Double(Math.log(opn.doubleValue())); break; // ln
			case 12: stk[si-1] = new Double(Math.log(opn.doubleValue()) / Math.log(10.0)); break; // log
			case 13: stk[si-2] = new Integer(opn.intValue() % stk[si-2].intValue()); si--; break; // mod
			case 14: stk[si-2] = new Double(stk[si-2].doubleValue() * opn.doubleValue()); si--; break; // mul
			case 15: stk[si-1] = new Double(- opn.doubleValue()); break; // neg
			case 16: stk[si-1] = new Double(Math.sin(opn.doubleValue())); break; // sin
			case 17: stk[si-1] = new Double(Math.sqrt(opn.doubleValue())); break; // sqrt
			case 18: stk[si-2] = new Double(stk[si-2].doubleValue() - opn.doubleValue()); si--; break; // sub
			case 19: stk[si-1] = new Integer(Math.round(opn.floatValue())); break; // round
			case 20: stk[si-1] = new Integer((int)opn.doubleValue()); break; // truncate

//			relational, boolean, bitwise
			case 21: stk[si-2] = new Integer(stk[i-2].intValue() & opn.intValue()); si--; break; // and (OK on bool)
			case 22: stk[si-2] = new Integer(stk[si-2].intValue() << opn.intValue()); si--; break; // bitshift
			case 23: stk[si-2] = (stk[i-2].doubleValue() == opn.doubleValue()? TRUE: FALSE); si--; break; // eq
			case 24: stk[si++] = FALSE; break; // false
			case 25: stk[si-2] = stk[i-2].doubleValue() >= opn.doubleValue()? TRUE: FALSE; si--; break; // ge
			case 26: {
				if (stk[i-2] == null){  // doudou 07.04.2006 bugfix cominto
					if (tmpEle != null)
						stk[si-2] = tmpEle.doubleValue() > opn.doubleValue()? TRUE: FALSE;
				}
				else
					stk[si-2] = stk[i-2].doubleValue() >  opn.doubleValue()? TRUE: FALSE;
					si--;
					break;} // gt
			case 27: stk[si-2] = stk[i-2].doubleValue() <= opn.doubleValue()? TRUE: FALSE; si--; break; // le
			case 28: stk[si-2] = stk[i-2].doubleValue() <  opn.doubleValue()? TRUE: FALSE; si--; break; // lt
			case 29: stk[si-2] = stk[i-2].doubleValue() != opn.doubleValue()? TRUE: FALSE; si--; break; // ne
			case 30: stk[si-1] = (opn==TRUE? FALSE: opn==FALSE? TRUE: new Integer(~ opn.intValue())); break; // not
			case 31: stk[si-2] = new Integer(stk[i-2].intValue() | opn.intValue()); si--; break; // or (OK on bool)
			case 32: stk[si++] = TRUE; break; // true
			case 33: stk[si-2] = new Integer(stk[i-2].intValue() ^ opn.intValue()); si--; break; // xor (OK on bool)

//			conditional
			case 34: si--; if (opn.intValue()!=0) si = execute(op.iftrue, stk, si); break; // if
			case 35: si--; si = execute((opn.intValue()!=0? op.iftrue: op.iffalse), stk, si); break; // ifelse

//			stack
			case 36: n=opn.intValue(); si--; System.arraycopy(stk,si-n, stk,si, n); si+=n; break; // copy
			case 37: stk[si-1]=stk[si-2]; stk[si-2]=opn; break; // exch
			case 38: si--; break; // pop
			case 39: stk[si++]=opn; tmpEle = opn; break; // dup (share immutable objects)     // doudou 07.04.2006 bugfix cominto
			case 40: n=opn.intValue(); si--; stk[si]=stk[si-n-1]; si++; break; // index
			case 41: n=stk[si-2].intValue(); j=opn.intValue(); /*...*/ break; // roll -- LATER

			}
		}

		return si;
	}

}
class Sampled extends  Function {

	private float[] domain,encode,decode;

	int[] size;

	int order=1;

	float[] samples;
	private int maxSize=0;
	boolean flateDecode = false;

	Sampled(byte[] stream,Map tintTransform,float[] domain,float[] range,PdfObjectReader currentPdfFile)  {
		//Check for filter to ensure correct decoding function is called
		
		if(tintTransform.get("Filter")!=null && tintTransform.get("Filter").equals("/FlateDecode"))
			flateDecode = true;
		else
			flateDecode = false;
		
		int BitsPerSample=Integer.parseInt((String) tintTransform.get("BitsPerSample"));

		this.maxSize=(int) Math.pow(2,BitsPerSample)-1;

		//set order
		String value=(String) tintTransform.get("Order");
		if (value != null)
			order=Integer.parseInt(value);

		//set size
		value=currentPdfFile.getValue((String) tintTransform.get("Size"));
		StringTokenizer matrixValues =new StringTokenizer(value, "[] ");
		size=new int[matrixValues.countTokens()];
		int i = 0;
		while (matrixValues.hasMoreTokens()) {
			size[i] = Integer.parseInt(matrixValues.nextToken());
			i++;
		}

		//set encode
		value=(String) tintTransform.get("Encode");
		if (value != null) {
			value=currentPdfFile.getValue(value);

			matrixValues =new StringTokenizer(value, "[] ");
			encode=new float[matrixValues.countTokens()];
			i = 0;
			while (matrixValues.hasMoreTokens()) {
				encode[i] = Float.parseFloat(matrixValues.nextToken());
				i++;
			}
		}else{
			int defaultSize=size.length;
			encode=new float[defaultSize*2];
			for(int ii=0;ii<defaultSize;ii++){
				encode[(ii*2)+1]=size[ii]-1;
			}
		}

		//set decode
		value=(String) tintTransform.get("Decode");
		if (value != null) {
			value=currentPdfFile.getValue(value);

			matrixValues =new StringTokenizer(value, "[] ");
			decode=new float[matrixValues.countTokens()];
			i = 0;
			while (matrixValues.hasMoreTokens()) {
				decode[i] = Float.parseFloat(matrixValues.nextToken());
				i++;
			}
		}else{
			int defaultSize=range.length;
			decode=new float[defaultSize];
			System.arraycopy(range, 0, decode, 0, defaultSize);
		}

		//set samples

		samples = new float[stream.length];
		try{
			InputStream in = new ByteArrayInputStream(stream);

			long bits=0L, mask=(1L<<BitsPerSample)-1L;

			float valmax=(1L<<BitsPerSample);     // float so no truncate on integer division

			for (int ii=0, valid=0; ii<stream.length; ii++) {
				while (valid<BitsPerSample) { bits = (bits<<8) + in.read(); valid+=8; }
				long samp = (bits >> (valid-BitsPerSample)) & mask;  valid -= BitsPerSample;
				samples[ii] = samp/valmax;    // sample is fraction of bit-range

			}
			in.close();
		}catch(Exception e){
			System.out.println("Exception "+e+" reading stream for separation colorspace");
		}

		this.domain=domain;
		this.range=range;

	}

	/**type 4 function*/
	final public String[] compute(float[] input,float[] domain){

		if(domain==null)
			domain=this.domain;

		int returnValues=range.length/2;

		float[] output=new float[returnValues];
		String[] result=new String[returnValues];

		try{

			compute(input,output,domain);

			for(int j=0;j<returnValues;j++)
				result[j]=""+output[returnValues-j-1];

		}catch(Exception e){
			System.out.println("Sampled exception "+e);
			e.printStackTrace();
		}

		return result;
	}

	boolean debugFunction=false;

	final public void compute(float[] input,float[] output,float[] domain){

		computejpedal(input,output,domain);
    }

	final public void computejpedal(float[] input,float[] output,float[] domain){
		//set values
		int m=domain.length/2;
		int n=range.length/2;
		float[] r=new float[n];
		
		//use X to match def in ref guide
		float[] x=input;
		float[] e=new float[m*2];

		for(int i=0;i<m;i++){
			//clip
			x[i]=min(max(x[i],domain[i*2]),domain[i*2+1]);

			//encode
			e[i*2]=interpolate(x[i],domain[i*2],domain[i*2+1],encode[i*2],encode[i*2+1]);

			//clip
			e[i*2]=min(max(e[i*2],0),size[i]-1);
			int t=0;
			t1=new int[m+1];

			t1[0]=0;
			for (int k=0;k<m; k++) {
				t=(t>0? t: 1)*size[k];
				t1[k+1]=t;

			}
			
			for(int j=0;j < n;j++){
				int value = (int)(e[i*2]);
				
					if((e[i*2]-(int)e[i*2])>0)
						value = (int)e[i*2]+1;
					
					float frac1 = value-e[i*2];
					float frac0=1f-frac1;
					
					//interpolate on fraction
					int lower=((int)e[i*2] *n)+j;
					int upper=(value * n)+j;

					r[j]=(frac1*samples[lower])+(frac0*(samples[upper]));

					//uses 1 and not maxSize as we have already factored in
					r[j]=interpolate(r[j],0,1,decode[j*2],decode[j*2+1]);

					output[j]=min(max(r[j],range[j*2]),range[j*2+1]);

			}
		}

    }

	private float interpolate(float x,float xmin,float xmax, float ymin, float ymax){

		return ((x-xmin)*(ymax-ymin)/(xmax-xmin))+ymin;

	}

	private float min(float a, float b) {

		if(a>b)
			return b;
		else
			return a;

	}

	private float max(float a, float b) {

		if(a<b)
			return b;
		else
			return a;

	}


	int[] t1;


    private void preCalcValues(float[] input, float[] domain, int m, int n, double[] e, double[] frac0, double[] frac1, int[] idxMul) {
		double x;
		int i;
		idxMul[0] = n;

		for (i = 1; i < m; i++){

			idxMul[i] = idxMul[i-1] * size[i-1];

		}

		if(debugFunction)
			System.out.println("----------------------------");
		// map input values into sample array
		for (i = 0; i < m; ++i) {
			float xmin=domain[i*2],xmax=domain[i*2+1], ymin=encode[i*2],ymax=encode[i*2+1];

			//inputMul[i]=1;
			x = ymin+((input[i] - xmin) * (ymax - ymin) /(xmax - xmin));
			if (x < 0) {
				x = 0;
			} else if (x > size[i] - 1){
				x = size[i] - 1;
			}

			e[i*2] = x;
			//System.out.println("bool=="+((e[i*2+1] = e[i*2] + 1) >= size[i])+"< first 1 =="+(e[i*2+1])+"< first 2 =="+(e[i*2])+"< last=="+size[i]+"<   i=="+i+"<");
			if ((e[i*2+1] = e[i*2] + 1) >= size[i]) {
				// this happens if in[i] = domain[i][1]
				e[i*2+1] = e[i*2];
			}

			frac1[i] = x - (int)e[i*2];
			frac0[i] = 1f - frac1[i];
		}
	}
}

/**
 Stitch together various 1-input subfunctions (all with same output arity).
 */
class Stitching extends Function {
	Function[] funs_;
	float[] bounds_;
	float[] encode_;
	int n_;

	Stitching(Map tintTransform,float[] domain,float[] range,PdfObjectReader currentPdfFile) {

		this.range=range;

		String value=currentPdfFile.getValue((String) tintTransform.get("Bounds"));
		if (value != null) {
			StringTokenizer matrixValues = new StringTokenizer(value, "[] ");
			bounds_ = new float[matrixValues.countTokens()];
			int i = 0;
			while (matrixValues.hasMoreTokens()) {
				bounds_[i] = Float.parseFloat(matrixValues.nextToken());
				i++;
			}
		}

		value=currentPdfFile.getValue((String) tintTransform.get("Encode"));
		if (value != null) {
			StringTokenizer matrixValues = new StringTokenizer(value, "[] ");
			encode_ = new float[matrixValues.countTokens()];
			int i = 0;
			while (matrixValues.hasMoreTokens()) {
				encode_[i] = Float.parseFloat(matrixValues.nextToken());
				i++;
			}
		}

		/**
		 * get functions
		 */
		String functionValues=(String) tintTransform.get("Functions");
		if(functionValues.indexOf("[")==-1) //allow for indirect object
			functionValues=(String) currentPdfFile.readObject(functionValues,false,null).get("rawValue");
		StringTokenizer functionIDs=new StringTokenizer(Strip.removeArrayDeleminators(functionValues),"R");
		int functionCount=functionIDs.countTokens();
		Map[]  subFunction=new Map[functionCount];

		for(int i=0;i<functionCount;i++){
			String id=(functionIDs.nextToken()+"R").trim();
			Map fn=currentPdfFile.readObject(id,false,null);
			//ensure stream decoded
			currentPdfFile.readStream(id,true);
			subFunction[i]=fn;
			//System.out.println(i+" subfunction>>"+fn);
		}


		funs_ = new Function[subFunction.length];

		for (int i=0,imax=subFunction.length; i<imax; i++){

			/**
			 * get values for sub stream Function
			 */
			if(subFunction[i].containsKey("CachedStream"))
				currentPdfFile.readStreamIntoMemory(subFunction[i]);

			byte[] subStream=(byte[]) subFunction[i].get("DecodedStream");

			int subFunctionType =Integer.parseInt((String) subFunction[i].get("FunctionType"));

			float[] subDomain=null;
			float[] subRange=null;
			value =(String) subFunction[i].get("Domain");
			if (value != null) {
				StringTokenizer matrixValues = new StringTokenizer(value, "[] ");
				subDomain = new float[matrixValues.countTokens()];
				int ii = 0;
				while (matrixValues.hasMoreTokens()) {
					subDomain[ii] = Float.parseFloat(matrixValues.nextToken());
					ii++;
				}
			}

			value = (String) subFunction[i].get("Range");
			if (value != null) {
				StringTokenizer matrixValues = new StringTokenizer(value, "[] ");
				subRange = new float[matrixValues.countTokens()];
				int ii = 0;
				while (matrixValues.hasMoreTokens()) {
					subRange[ii] = Float.parseFloat(matrixValues.nextToken());
					ii++;
				}
			}

			funs_[i] = Function.getInstance(subStream,subFunction[i],subDomain,
					subRange,subFunctionType,currentPdfFile);
		}

		n_ = funs_[0].getN();   // all should be the same

		value=currentPdfFile.getValue((String) tintTransform.get("Bounds"));
		StringTokenizer matrixValues =new StringTokenizer(value, "[] ");
		bounds_=new float[matrixValues.countTokens()];
		int i = 0;
		while (matrixValues.hasMoreTokens()) {
			bounds_[i] =Float.parseFloat(matrixValues.nextToken());
			i++;
		}

		value=currentPdfFile.getValue((String) tintTransform.get("Encode"));
		matrixValues =new StringTokenizer(value, "[] ");
		encode_=new float[matrixValues.countTokens()];
		i = 0;
		while (matrixValues.hasMoreTokens()) {
			encode_[i] =Float.parseFloat(matrixValues.nextToken());
			i++;
		}
	}

	public int getN() { return n_; }  // /Range not required

	final public String[] compute(float[] input,float[] domain){


		//if(domain==null)
		//domain=this.domain;

		int returnValues=getN();
		if(range!=null)
			returnValues=range.length/2;
//		else {
//		returnValues=this.colorCount;
//		if(returnValues==3)
//		System.exit(1);
//		}
		float[] output=new float[returnValues];
		String[] result=new String[returnValues];
		try{
			compute(input,output,domain);

			for(int j=0;j<returnValues;j++)
				result[j]=""+output[returnValues-j-1];

		}catch(Exception e){
			System.out.println("Compute error "+e);
		}

		return result;
	}

	public void compute(float[] input, float[] output,float[] domain) {

		clip(input, domain);

		//take raw input number
		float x = input[0];

		//see if value lies outside a boundary
		int subi=bounds_.length-1;
		for (; subi>=0; subi--) if (x >= bounds_[subi]) break;
		subi++;


		//if it does, truncate it
		float[] subinput = new float[1];
		float xmin=domain[0],xmax=domain[1];
		if(subi>0)
			xmin=(bounds_[subi-1]);
		if(subi<bounds_.length)
			xmax=(bounds_[subi]);

		//System.err.println("x,xmin,xmax="+x+" "+xmin+" "+xmax);
		float ymin=encode_[subi*2];
		float ymax=encode_[subi*2+1];
		subinput[0] = ymin + ((x-xmin) * (ymax-ymin)/(xmax-xmin));  // interpolate

//		System.err.println("ymin,ymax= "+ymin+" "+ymax);
		//	System.err.println("RETURns="+subinput[0]);

		//if(x>0.79774)
		//	subinput[0] =0f;
		funs_[subi].compute(subinput, output,domain);
		//if(subi!=0)
		//System.exit(1);
		if (range!=null) clip(output, range);
	}
}
//<end-13>