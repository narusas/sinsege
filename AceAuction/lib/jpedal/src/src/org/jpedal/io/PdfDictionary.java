package org.jpedal.io;

class PdfDictionary {

	private static int[] indices={24,16,8,0};
	public static String getKey(char[] nextKey, int keyPt) {
		
		String key=null;
		/** int pointer=0,ptr=0;
		
		//generate unique key using last 4 keys values
		for(int ii=0;ii<4;ii++){
			ptr=keyPt-4+ii;
			
			if(ptr>=0){
				pointer=pointer+nextKey[ptr] <<indices[ii];
				//System.out.println(1<<indices[ii]);
				//System.out.println(pointer+" "+String.copyValueOf(nextKey,0,keyPt));
			}
		}
		/**
		switch(pointer){
		case 1107324792:
			key="MediaBox";
			break;
		case 1660970355:
			key="Resources";
			break;
		case 1728079219:
			key="Pages";
			break;
		case 1761633395:
			key="Kids";
			break;
		case 1962962548:
			key="Count";
			break;
			case 2030071909:
			key="Type";
			break;
				
			default:
				key=String.copyValueOf(nextKey,0,keyPt);
			//System.out.println("case "+pointer+":\nkey=\""+String.copyValueOf(nextKey,0,keyPt)+"\";\nbreak;");
			//	System.out.println(pointer+" "+String.copyValueOf(nextKey,0,keyPt)+" "+keyPt);
			//System.exit(1);
				break;
		}
		*/
		
		return String.copyValueOf(nextKey,0,keyPt);
	}

}
