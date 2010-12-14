package org.jpedal.color;

import java.util.Map;
import java.util.StringTokenizer;
import java.io.Serializable;

import org.jpedal.function.Function;
import org.jpedal.io.PdfObjectReader;

public class ColorMapping implements Serializable {
	
//	<start-13>
	protected Function fn;
	//<end-13>

    protected float[] domain;
    
	
	public ColorMapping (PdfObjectReader currentPdfFile, Map tintTransform, byte[] stream, float[] range) {

        int functionType =Integer.parseInt((String) tintTransform.get("FunctionType"));

		String value =currentPdfFile.getValue((String) tintTransform.get("Domain"));
		if (value != null) {
			StringTokenizer matrixValues = new StringTokenizer(value, "[] ");
			domain = new float[matrixValues.countTokens()];
			int i = 0;
			while (matrixValues.hasMoreTokens()) {
				domain[i] = Float.parseFloat(matrixValues.nextToken());
				i++;
			}
		}

		value = currentPdfFile.getValue((String) tintTransform.get("Range"));
		if (value != null) {
			StringTokenizer matrixValues = new StringTokenizer(value, "[] ");
			range = new float[matrixValues.countTokens()];
			int i = 0;
			while (matrixValues.hasMoreTokens()) {
				range[i] = Float.parseFloat(matrixValues.nextToken());
				i++;
			}
		}
		
		//<start-13>
		/** setup the translation function */
		fn =Function.getInstance(
				stream,
				tintTransform,
				domain,
				range,
				functionType,
				currentPdfFile);
		
		this.domain=domain;
		
		//<end-13>
	}
	
	//<start-13>
	public String[] getOperand(float[] values){
		
		return fn.compute(values,domain);
		
	}
	//<end-13>

    public float[] getDomain() {
        return domain; 
    }
}
