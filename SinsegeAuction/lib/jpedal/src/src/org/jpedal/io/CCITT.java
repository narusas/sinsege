package org.jpedal.io;

public class CCITT {

	public short bits;
	public short n;

	public short ccittEOL=-2;
	public short twoDimPass=0;
	public short twoDimHoriz=1;
	public short twoDimVert0=2;
	public short twoDimVertR1=3;
	public short twoDimVertL1=4;
	public short twoDimVertR2=5;
	public short twoDimVertL2=6;
	public short twoDimVertR3=7;
	public short twoDimVertL3=8;
	CCITT twoDimTab1[] = null;
	CCITT blackTab1[] = null;
	CCITT blackTab2[] = null;
	CCITT blackTab3[] = null;
	CCITT whiteTab1[] = null;
	CCITT whiteTab2[] = null;
	public CCITT(short bits, short n) {
		this.bits=bits;
		this.n=n;
	};


	public CCITT(){



//		1-7 bit codes
		CCITT twoDimTab1[] = {
				new CCITT((short)-1,(short) -1), new CCITT((short)-1,(short)-1),		        // 000000x
				new CCITT((short)7, twoDimVertL3),		        // 0000010
				new CCITT((short)7, twoDimVertR3),		        // 0000011
				new CCITT((short)6, twoDimVertL2), new CCITT((short)6, twoDimVertL2),	// 000010x
				new CCITT((short)6, twoDimVertR2), new CCITT((short)6, twoDimVertR2),	// 000011x
				new CCITT((short)4, twoDimPass), new CCITT((short)4, twoDimPass),     // 0001xxx
				new CCITT((short)4, twoDimPass), new CCITT((short)4, twoDimPass),
				new CCITT((short)4, twoDimPass), new CCITT((short)4, twoDimPass),
				new CCITT((short)4, twoDimPass), new CCITT((short)4, twoDimPass),
				new CCITT((short)3, twoDimHoriz), new CCITT((short)3, twoDimHoriz),	// 001xxxx
				new CCITT((short)3, twoDimHoriz), new CCITT((short)3, twoDimHoriz),
				new CCITT((short)3, twoDimHoriz), new CCITT((short)3, twoDimHoriz),
				new CCITT((short)3, twoDimHoriz), new CCITT((short)3, twoDimHoriz),
				new CCITT((short)3, twoDimHoriz), new CCITT((short)3, twoDimHoriz),
				new CCITT((short)3, twoDimHoriz), new CCITT((short)3, twoDimHoriz),
				new CCITT((short)3, twoDimHoriz), new CCITT((short)3, twoDimHoriz),
				new CCITT((short)3, twoDimHoriz), new CCITT((short)3, twoDimHoriz),
				new CCITT((short)3, twoDimVertL1), new CCITT((short)3, twoDimVertL1),	// 010xxxx
				new CCITT((short)3, twoDimVertL1), new CCITT((short)3, twoDimVertL1),
				new CCITT((short)3, twoDimVertL1), new CCITT((short)3, twoDimVertL1),
				new CCITT((short)3, twoDimVertL1), new CCITT((short)3, twoDimVertL1),
				new CCITT((short)3, twoDimVertL1), new CCITT((short)3, twoDimVertL1),
				new CCITT((short)3, twoDimVertL1), new CCITT((short)3, twoDimVertL1),
				new CCITT((short)3, twoDimVertL1), new CCITT((short)3, twoDimVertL1),
				new CCITT((short)3, twoDimVertL1), new CCITT((short)3, twoDimVertL1),
				new CCITT((short)3, twoDimVertR1), new CCITT((short)3, twoDimVertR1),	// 011xxxx
				new CCITT((short)3, twoDimVertR1), new CCITT((short)3, twoDimVertR1),
				new CCITT((short)3, twoDimVertR1), new CCITT((short)3, twoDimVertR1),
				new CCITT((short)3, twoDimVertR1), new CCITT((short)3, twoDimVertR1),
				new CCITT((short)3, twoDimVertR1), new CCITT((short)3, twoDimVertR1),
				new CCITT((short)3, twoDimVertR1), new CCITT((short)3, twoDimVertR1),
				new CCITT((short)3, twoDimVertR1), new CCITT((short)3, twoDimVertR1),
				new CCITT((short)3, twoDimVertR1), new CCITT((short)3, twoDimVertR1),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),	// 1xxxxxx
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0),
				new CCITT((short)1, twoDimVert0), new CCITT((short)1, twoDimVert0)
		};

//		------------------------------------------------------------------------
//		white run lengths
//		------------------------------------------------------------------------

//		11-12 bit codes (upper 7 bits are 0)
		CCITT whiteTab1[] = {
				new CCITT((short)-1, (short)-1),					// 00000
				new CCITT((short)12, ccittEOL),				// 00001
				new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1),				// 0001x
				new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1),	// 001xx
				new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1),	// 010xx
				new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1),	// 011xx
				new CCITT((short)11, (short)1792), new CCITT((short)11, (short)1792),			// 1000x
				new CCITT((short)12, (short)1984),					// 10010
				new CCITT((short)12, (short)2048),					// 10011
				new CCITT((short)12, (short)2112),					// 10100
				new CCITT((short)12, (short)2176),					// 10101
				new CCITT((short)12, (short)2240),					// 10110
				new CCITT((short)12, (short)2304),					// 10111
				new CCITT((short)11, (short)1856), new CCITT((short)11, (short)1856),			// 1100x
				new CCITT((short)11, (short)1920), new CCITT((short)11, (short)1920),			// 1101x
				new CCITT((short)12, (short)2368),					// 11100
				new CCITT((short)12, (short)2432),					// 11101
				new CCITT((short)12, (short)2496),					// 11110
				new CCITT((short)12, (short)2560)					// 11111
		};

//		1-9 bit codes
		CCITT whiteTab2[] = {
				new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1),	// 0000000xx
				new CCITT((short)8, (short)29), new CCITT((short)8, (short)29),				// 00000010x
				new CCITT((short)8, (short)30), new CCITT((short)8, (short)30),				// 00000011x
				new CCITT((short)8, (short)45), new CCITT((short)8, (short)45),				// 00000100x
				new CCITT((short)8, (short)46), new CCITT((short)8, (short)46),				// 00000101x
				new CCITT((short)7, (short)22), new CCITT((short)7, (short)22), new CCITT((short)7, (short)22), new CCITT((short)7, (short)22),		// 0000011xx
				new CCITT((short)7, (short)23), new CCITT((short)7, (short)23), new CCITT((short)7, (short)23), new CCITT((short)7, (short)23),		// 0000100xx
				new CCITT((short)8, (short)47), new CCITT((short)8, (short)47),				// 00001010x
				new CCITT((short)8, (short)48), new CCITT((short)8, (short)48),				// 00001011x
				new CCITT((short)6, (short)13), new CCITT((short)6, (short)13), new CCITT((short)6, (short)13), new CCITT((short)6, (short)13),		// 000011xxx
				new CCITT((short)6, (short)13), new CCITT((short)6, (short)13), new CCITT((short)6, (short)13), new CCITT((short)6, (short)13),
				new CCITT((short)7, (short)20), new CCITT((short)7, (short)20), new CCITT((short)7, (short)20), new CCITT((short)7, (short)20),		// 0001000xx
				new CCITT((short)8, (short)33), new CCITT((short)8, (short)33),				// 00010010x
				new CCITT((short)8, (short)34), new CCITT((short)8, (short)34),				// 00010011x
				new CCITT((short)8, (short)35), new CCITT((short)8, (short)35),				// 00010100x
				new CCITT((short)8, (short)36), new CCITT((short)8, (short)36),				// 00010101x
				new CCITT((short)8, (short)37), new CCITT((short)8, (short)37),				// 00010110x
				new CCITT((short)8, (short)38), new CCITT((short)8, (short)38),				// 00010111x
				new CCITT((short)7, (short)19), new CCITT((short)7, (short)19), new CCITT((short)7, (short)19), new CCITT((short)7, (short)19),		// 0001100xx
				new CCITT((short)8, (short)31), new CCITT((short)8, (short)31),				// 00011010x
				new CCITT((short)8, (short)32), new CCITT((short)8, (short)32),				// 00011011x
				new CCITT((short)6, (short)1), new CCITT((short)6, (short)1), new CCITT((short)6, (short)1), new CCITT((short)6, (short)1),		// 000111xxx
				new CCITT((short)6, (short)1), new CCITT((short)6, (short)1), new CCITT((short)6, (short)1), new CCITT((short)6, (short)1),
				new CCITT((short)6, (short)12), new CCITT((short)6, (short)12), new CCITT((short)6, (short)12), new CCITT((short)6, (short)12),		// 001000xxx
				new CCITT((short)6, (short)12), new CCITT((short)6, (short)12), new CCITT((short)6, (short)12), new CCITT((short)6, (short)12),
				new CCITT((short)8, (short)53), new CCITT((short)8, (short)53),				// 00100100x
				new CCITT((short)8, (short)54), new CCITT((short)8, (short)54),				// 00100101x
				new CCITT((short)7, (short)26), new CCITT((short)7, (short)26), new CCITT((short)7, (short)26), new CCITT((short)7, (short)26),		// 0010011xx
				new CCITT((short)8, (short)39), new CCITT((short)8, (short)39),				// 00101000x
				new CCITT((short)8, (short)40), new CCITT((short)8, (short)40),				// 00101001x
				new CCITT((short)8, (short)41), new CCITT((short)8, (short)41),				// 00101010x
				new CCITT((short)8, (short)42), new CCITT((short)8, (short)42),				// 00101011x
				new CCITT((short)8, (short)43), new CCITT((short)8, (short)43),				// 00101100x
				new CCITT((short)8, (short)44), new CCITT((short)8, (short)44),				// 00101101x
				new CCITT((short)7, (short)21), new CCITT((short)7, (short)21), new CCITT((short)7, (short)21), new CCITT((short)7, (short)21),		// 0010111xx
				new CCITT((short)7, (short)28), new CCITT((short)7, (short)28), new CCITT((short)7, (short)28), new CCITT((short)7, (short)28),		// 0011000xx
				new CCITT((short)8, (short)61), new CCITT((short)8, (short)61),				// 00110010x
				new CCITT((short)8, (short)62), new CCITT((short)8, (short)62),				// 00110011x
				new CCITT((short)8, (short)63), new CCITT((short)8, (short)63),				// 00110100x
				new CCITT((short)8, (short)0), new CCITT((short)8, (short)0),				// 00110101x
				new CCITT((short)8, (short)320), new CCITT((short)8, (short)320),				// 00110110x
				new CCITT((short)8, (short)384), new CCITT((short)8, (short)384),				// 00110111x
				new CCITT((short)5, (short)10), new CCITT((short)5, (short)10), new CCITT((short)5, (short)10), new CCITT((short)5, (short)10),		// 00111xxxx
				new CCITT((short)5, (short)10), new CCITT((short)5, (short)10), new CCITT((short)5, (short)10), new CCITT((short)5, (short)10),
				new CCITT((short)5, (short)10), new CCITT((short)5, (short)10), new CCITT((short)5, (short)10), new CCITT((short)5, (short)10),
				new CCITT((short)5, (short)10), new CCITT((short)5, (short)10), new CCITT((short)5, (short)10), new CCITT((short)5, (short)10),
				new CCITT((short)5, (short)11), new CCITT((short)5, (short)11), new CCITT((short)5, (short)11), new CCITT((short)5, (short)11),		// 01000xxxx
				new CCITT((short)5, (short)11), new CCITT((short)5, (short)11), new CCITT((short)5, (short)11), new CCITT((short)5, (short)11),
				new CCITT((short)5, (short)11), new CCITT((short)5, (short)11), new CCITT((short)5, (short)11), new CCITT((short)5, (short)11),
				new CCITT((short)5, (short)11), new CCITT((short)5, (short)11), new CCITT((short)5, (short)11), new CCITT((short)5, (short)11),
				new CCITT((short)7, (short)27), new CCITT((short)7, (short)27), new CCITT((short)7, (short)27), new CCITT((short)7, (short)27),		// 0100100xx
				new CCITT((short)8, (short)59), new CCITT((short)8, (short)59),				// 01001010x
				new CCITT((short)8, (short)60), new CCITT((short)8, (short)60),				// 01001011x
				new CCITT((short)9, (short)1472),					// 010011000
				new CCITT((short)9, (short)1536),					// 010011001
				new CCITT((short)9, (short)1600),					// 010011010
				new CCITT((short)9, (short)1728),					// 010011011
				new CCITT((short)7, (short)18), new CCITT((short)7, (short)18), new CCITT((short)7, (short)18), new CCITT((short)7, (short)18),		// 0100111xx
				new CCITT((short)7, (short)24), new CCITT((short)7, (short)24), new CCITT((short)7, (short)24), new CCITT((short)7, (short)24),		// 0101000xx
				new CCITT((short)8, (short)49), new CCITT((short)8, (short)49),				// 01010010x
				new CCITT((short)8, (short)50), new CCITT((short)8, (short)50),				// 01010011x
				new CCITT((short)8, (short)51), new CCITT((short)8, (short)51),				// 01010100x
				new CCITT((short)8, (short)52), new CCITT((short)8, (short)52),				// 01010101x
				new CCITT((short)7, (short)25), new CCITT((short)7, (short)25), new CCITT((short)7, (short)25), new CCITT((short)7, (short)25),		// 0101011xx
				new CCITT((short)8, (short)55), new CCITT((short)8, (short)55),				// 01011000x
				new CCITT((short)8, (short)56), new CCITT((short)8, (short)56),				// 01011001x
				new CCITT((short)8, (short)57), new CCITT((short)8, (short)57),				// 01011010x
				new CCITT((short)8, (short)58), new CCITT((short)8, (short)58),				// 01011011x
				new CCITT((short)6, (short)192), new CCITT((short)6, (short)192), new CCITT((short)6, (short)192), new CCITT((short)6, (short)192),	// 010111xxx
				new CCITT((short)6, (short)192), new CCITT((short)6, (short)192), new CCITT((short)6, (short)192), new CCITT((short)6, (short)192),
				new CCITT((short)6, (short)1664), new CCITT((short)6, (short)1664), new CCITT((short)6, (short)1664), new CCITT((short)6, (short)1664),	// 011000xxx
				new CCITT((short)6, (short)1664), new CCITT((short)6, (short)1664), new CCITT((short)6, (short)1664), new CCITT((short)6, (short)1664),
				new CCITT((short)8, (short)448), new CCITT((short)8, (short)448),				// 01100100x
				new CCITT((short)8, (short)512), new CCITT((short)8, (short)512),				// 01100101x
				new CCITT((short)9, (short)704),					// 011001100
				new CCITT((short)9, (short)768),					// 011001101
				new CCITT((short)8, (short)640), new CCITT((short)8, (short)640),				// 01100111x
				new CCITT((short)8, (short)576), new CCITT((short)8, (short)576),				// 01101000x
				new CCITT((short)9, (short)832),					// 011010010
				new CCITT((short)9, (short)896),					// 011010011
				new CCITT((short)9, (short)960),					// 011010100
				new CCITT((short)9, (short)1024),					// 011010101
				new CCITT((short)9, (short)1088),					// 011010110
				new CCITT((short)9, (short)1152),					// 011010111
				new CCITT((short)9, (short)1216),					// 011011000
				new CCITT((short)9, (short)1280),					// 011011001
				new CCITT((short)9, (short)1344),					// 011011010
				new CCITT((short)9, (short)1408),					// 011011011
				new CCITT((short)7, (short)(short)256), new CCITT((short)7, (short)256), new CCITT((short)7, (short)256), new CCITT((short)7, (short)256),	// 0110111xx
				new CCITT((short)4, (short)2), new CCITT((short)4, (short)2), new CCITT((short)4, (short)2), new CCITT((short)4, (short)2),		// 0111xxxxx
				new CCITT((short)4, (short)2), new CCITT((short)4, (short)2), new CCITT((short)4, (short)2), new CCITT((short)4, (short)2),
				new CCITT((short)4, (short)2), new CCITT((short)4, (short)2), new CCITT((short)4, (short)2), new CCITT((short)4, (short)2),
				new CCITT((short)4, (short)2), new CCITT((short)4, (short)2), new CCITT((short)4, (short)2), new CCITT((short)4, (short)2),
				new CCITT((short)4, (short)2), new CCITT((short)4, (short)2), new CCITT((short)4, (short)2), new CCITT((short)4, (short)2),
				new CCITT((short)4, (short)2), new CCITT((short)4, (short)2), new CCITT((short)4, (short)2), new CCITT((short)4, (short)2),
				new CCITT((short)4, (short)2), new CCITT((short)4, (short)2), new CCITT((short)4, (short)2), new CCITT((short)4, (short)2),
				new CCITT((short)4, (short)2), new CCITT((short)4, (short)2), new CCITT((short)4, (short)2), new CCITT((short)4, (short)2),
				new CCITT((short)4, (short)3), new CCITT((short)4, (short)3), new CCITT((short)4, (short)3), new CCITT((short)4, (short)3),		// 1000xxxxx
				new CCITT((short)4, (short)3), new CCITT((short)4, (short)3), new CCITT((short)4, (short)3), new CCITT((short)4, (short)3),
				new CCITT((short)4, (short)3), new CCITT((short)4, (short)3), new CCITT((short)4, (short)3), new CCITT((short)4, (short)3),
				new CCITT((short)4, (short)3), new CCITT((short)4, (short)3), new CCITT((short)4, (short)3), new CCITT((short)4, (short)3),
				new CCITT((short)4, (short)3), new CCITT((short)4, (short)3), new CCITT((short)4, (short)3), new CCITT((short)4, (short)3),
				new CCITT((short)4, (short)3), new CCITT((short)4, (short)3), new CCITT((short)4, (short)3), new CCITT((short)4, (short)3),
				new CCITT((short)4, (short)3), new CCITT((short)4, (short)3), new CCITT((short)4, (short)3), new CCITT((short)4, (short)3),
				new CCITT((short)4, (short)3), new CCITT((short)4, (short)3), new CCITT((short)4, (short)3), new CCITT((short)4, (short)3),
				new CCITT((short)5, (short)128), new CCITT((short)5, (short)128), new CCITT((short)5, (short)128), new CCITT((short)5, (short)128),	// 10010xxxx
				new CCITT((short)5, (short)128), new CCITT((short)5, (short)128), new CCITT((short)5, (short)128), new CCITT((short)5, (short)128),
				new CCITT((short)5, (short)128), new CCITT((short)5, (short)128), new CCITT((short)5, (short)128), new CCITT((short)5, (short)128),
				new CCITT((short)5, (short)128), new CCITT((short)5, (short)128), new CCITT((short)5, (short)128), new CCITT((short)5, (short)128),
				new CCITT((short)5, (short)8), new CCITT((short)5, (short)8), new CCITT((short)5, (short)8), new CCITT((short)5, (short)8),		// 10011xxxx
				new CCITT((short)5, (short)8), new CCITT((short)5, (short)8), new CCITT((short)5, (short)8), new CCITT((short)5, (short)8),
				new CCITT((short)5, (short)8), new CCITT((short)5, (short)8), new CCITT((short)5, (short)8), new CCITT((short)5, (short)8),
				new CCITT((short)5, (short)8), new CCITT((short)5, (short)8), new CCITT((short)5, (short)8), new CCITT((short)5, (short)8),
				new CCITT((short)5, (short)9), new CCITT((short)5, (short)9), new CCITT((short)5, (short)9), new CCITT((short)5, (short)9),		// 10100xxxx
				new CCITT((short)5, (short)9), new CCITT((short)5, (short)9), new CCITT((short)5, (short)9), new CCITT((short)5, (short)9),
				new CCITT((short)5, (short)9), new CCITT((short)5, (short)9), new CCITT((short)5, (short)9), new CCITT((short)5, (short)9),
				new CCITT((short)5, (short)9), new CCITT((short)5, (short)9), new CCITT((short)5, (short)9), new CCITT((short)5, (short)9),
				new CCITT((short)6, (short)16), new CCITT((short)6, (short)16), new CCITT((short)6, (short)16), new CCITT((short)6, (short)16),		// 101010xxx
				new CCITT((short)6, (short)16), new CCITT((short)6, (short)16), new CCITT((short)6, (short)16), new CCITT((short)6, (short)16),
				new CCITT((short)6, (short)17), new CCITT((short)6, (short)17), new CCITT((short)6, (short)17), new CCITT((short)6, (short)17),		// 101011xxx
				new CCITT((short)6, (short)17), new CCITT((short)6, (short)17), new CCITT((short)6, (short)17), new CCITT((short)6, (short)17),
				new CCITT((short)4, (short)4), new CCITT((short)4, (short)4), new CCITT((short)4, (short)4), new CCITT((short)4, (short)4),		// 1011xxxxx
				new CCITT((short)4, (short)4), new CCITT((short)4, (short)4), new CCITT((short)4, (short)4), new CCITT((short)4, (short)4),
				new CCITT((short)4, (short)4), new CCITT((short)4, (short)4), new CCITT((short)4, (short)4), new CCITT((short)4, (short)4),
				new CCITT((short)4, (short)4), new CCITT((short)4, (short)4), new CCITT((short)4, (short)4), new CCITT((short)4, (short)4),
				new CCITT((short)4, (short)4), new CCITT((short)4, (short)4), new CCITT((short)4, (short)4), new CCITT((short)4, (short)4),
				new CCITT((short)4, (short)4), new CCITT((short)4, (short)4), new CCITT((short)4, (short)4), new CCITT((short)4, (short)4),
				new CCITT((short)4, (short)4), new CCITT((short)4, (short)4), new CCITT((short)4, (short)4), new CCITT((short)4, (short)4),
				new CCITT((short)4, (short)4), new CCITT((short)4, (short)4), new CCITT((short)4, (short)4), new CCITT((short)4, (short)4),
				new CCITT((short)4, (short)5), new CCITT((short)4, (short)5), new CCITT((short)4, (short)5), new CCITT((short)4, (short)5),		// 1100xxxxx
				new CCITT((short)4, (short)5), new CCITT((short)4, (short)5), new CCITT((short)4, (short)5), new CCITT((short)4, (short)5),
				new CCITT((short)4, (short)5), new CCITT((short)4, (short)5), new CCITT((short)4, (short)5), new CCITT((short)4, (short)5),
				new CCITT((short)4, (short)5), new CCITT((short)4, (short)5), new CCITT((short)4, (short)5), new CCITT((short)4, (short)5),
				new CCITT((short)4, (short)5), new CCITT((short)4, (short)5), new CCITT((short)4, (short)5), new CCITT((short)4, (short)5),
				new CCITT((short)4, (short)5), new CCITT((short)4, (short)5), new CCITT((short)4, (short)5), new CCITT((short)4, (short)5),
				new CCITT((short)4, (short)5), new CCITT((short)4, (short)5), new CCITT((short)4, (short)5), new CCITT((short)4, (short)5),
				new CCITT((short)4, (short)5), new CCITT((short)4, (short)5), new CCITT((short)4, (short)5), new CCITT((short)4, (short)5),
				new CCITT((short)6, (short)14), new CCITT((short)6, (short)14), new CCITT((short)6, (short)14), new CCITT((short)6, (short)14),		// 110100xxx
				new CCITT((short)6, (short)14), new CCITT((short)6, (short)14), new CCITT((short)6, (short)14), new CCITT((short)6, (short)14),
				new CCITT((short)6, (short)15), new CCITT((short)6, (short)15), new CCITT((short)6, (short)15), new CCITT((short)6, (short)15),		// 110101xxx
				new CCITT((short)6, (short)15), new CCITT((short)6, (short)15), new CCITT((short)6, (short)15), new CCITT((short)6, (short)15),
				new CCITT((short)5, (short)64), new CCITT((short)5, (short)64), new CCITT((short)5, (short)64), new CCITT((short)5, (short)64),		// 11011xxxx
				new CCITT((short)5, (short)64), new CCITT((short)5, (short)64), new CCITT((short)5, (short)64), new CCITT((short)5, (short)64),
				new CCITT((short)5, (short)64), new CCITT((short)5, (short)64), new CCITT((short)5, (short)64), new CCITT((short)5, (short)64),
				new CCITT((short)5, (short)64), new CCITT((short)5, (short)64), new CCITT((short)5, (short)64), new CCITT((short)5, (short)64),
				new CCITT((short)4, (short)6), new CCITT((short)4, (short)6), new CCITT((short)4, (short)6), new CCITT((short)4, (short)6),		// 1110xxxxx
				new CCITT((short)4, (short)6), new CCITT((short)4, (short)6), new CCITT((short)4, (short)6), new CCITT((short)4, (short)6),
				new CCITT((short)4, (short)6), new CCITT((short)4, (short)6), new CCITT((short)4, (short)6), new CCITT((short)4, (short)6),
				new CCITT((short)4, (short)6), new CCITT((short)4, (short)6), new CCITT((short)4, (short)6), new CCITT((short)4, (short)6),
				new CCITT((short)4, (short)6), new CCITT((short)4, (short)6), new CCITT((short)4, (short)6), new CCITT((short)4, (short)6),
				new CCITT((short)4, (short)6), new CCITT((short)4, (short)6), new CCITT((short)4, (short)6), new CCITT((short)4, (short)6),
				new CCITT((short)4, (short)6), new CCITT((short)4, (short)6), new CCITT((short)4, (short)6), new CCITT((short)4, (short)6),
				new CCITT((short)4, (short)6), new CCITT((short)4, (short)6), new CCITT((short)4, (short)6), new CCITT((short)4, (short)6),
				new CCITT((short)4, (short)7), new CCITT((short)4, (short)7), new CCITT((short)4, (short)7), new CCITT((short)4, (short)7),		// 1111xxxxx
				new CCITT((short)4, (short)7), new CCITT((short)4, (short)7), new CCITT((short)4, (short)7), new CCITT((short)4, (short)7),
				new CCITT((short)4, (short)7), new CCITT((short)4, (short)7), new CCITT((short)4, (short)7), new CCITT((short)4, (short)7),
				new CCITT((short)4, (short)7), new CCITT((short)4, (short)7), new CCITT((short)4, (short)7), new CCITT((short)4, (short)7),
				new CCITT((short)4, (short)7), new CCITT((short)4, (short)7), new CCITT((short)4, (short)7), new CCITT((short)4, (short)7),
				new CCITT((short)4, (short)7), new CCITT((short)4, (short)7), new CCITT((short)4, (short)7), new CCITT((short)4, (short)7),
				new CCITT((short)4, (short)7), new CCITT((short)4, (short)7), new CCITT((short)4, (short)7), new CCITT((short)4, (short)7),
				new CCITT((short)4, (short)7), new CCITT((short)4, (short)7), new CCITT((short)4, (short)7), new CCITT((short)4, (short)7)
		};

//		------------------------------------------------------------------------
//		black run lengths
//		------------------------------------------------------------------------

//		10-13 bit codes (upper 6 bits are 0)
		CCITT blackTab1[] = {
				new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1),					// 000000000000x
				new CCITT((short)12, ccittEOL), new CCITT((short)12, ccittEOL),			// 000000000001x
				new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1),		// 00000000001xx
				new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1),		// 00000000010xx
				new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1),		// 00000000011xx
				new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1),		// 00000000100xx
				new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1),		// 00000000101xx
				new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1),		// 00000000110xx
				new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1),		// 00000000111xx
				new CCITT((short)11, (short)1792), new CCITT((short)11, (short)1792), new CCITT((short)11, (short)1792), new CCITT((short)11, (short)1792),	// 00000001000xx
				new CCITT((short)12, (short)1984), new CCITT((short)12, (short)1984),				// 000000010010x
				new CCITT((short)12, (short)2048), new CCITT((short)12, (short)2048),				// 000000010011x
				new CCITT((short)12, (short)2112), new CCITT((short)12, (short)2112),				// 000000010100x
				new CCITT((short)12, (short)2176), new CCITT((short)12, (short)2176),				// 000000010101x
				new CCITT((short)12, (short)2240), new CCITT((short)12, (short)2240),				// 000000010110x
				new CCITT((short)12, (short)2304), new CCITT((short)12, (short)2304),				// 000000010111x
				new CCITT((short)11, (short)1856), new CCITT((short)11, (short)1856), new CCITT((short)11, (short)1856), new CCITT((short)11, (short)1856),	// 00000001100xx
				new CCITT((short)11, (short)1920), new CCITT((short)11, (short)1920), new CCITT((short)11, (short)1920), new CCITT((short)11, (short)1920),	// 00000001101xx
				new CCITT((short)12, (short)2368), new CCITT((short)12, (short)2368),				// 000000011100x
				new CCITT((short)12, (short)2432), new CCITT((short)12, (short)2432),				// 000000011101x
				new CCITT((short)12, (short)2496), new CCITT((short)12, (short)2496),				// 000000011110x
				new CCITT((short)12, (short)2560), new CCITT((short)12, (short)2560),				// 000000011111x
				new CCITT((short)10, (short)18), new CCITT((short)10, (short)18), new CCITT((short)10, (short)18), new CCITT((short)10, (short)18),		// 0000001000xxx
				new CCITT((short)10, (short)18), new CCITT((short)10, (short)18), new CCITT((short)10, (short)18), new CCITT((short)10, (short)18),
				new CCITT((short)12, (short)52), new CCITT((short)12, (short)52),					// 000000100100x
				new CCITT((short)13, (short)640),						// 0000001001010
				new CCITT((short)13, (short)704),						// 0000001001011
				new CCITT((short)13, (short)768),						// 0000001001100
				new CCITT((short)13, (short)832),						// 0000001001101
				new CCITT((short)12, (short)55), new CCITT((short)12, (short)55),					// 000000100111x
				new CCITT((short)12, (short)56), new CCITT((short)12, (short)56),					// 000000101000x
				new CCITT((short)13, (short)1280),						// 0000001010010
				new CCITT((short)13, (short)1344),						// 0000001010011
				new CCITT((short)13, (short)1408),						// 0000001010100
				new CCITT((short)13, (short)1472),						// 0000001010101
				new CCITT((short)12, (short)59), new CCITT((short)12, (short)59),					// 000000101011x
				new CCITT((short)12, (short)60), new CCITT((short)12, (short)60),					// 000000101100x
				new CCITT((short)13, (short)1536),						// 0000001011010
				new CCITT((short)13, (short)1600),						// 0000001011011
				new CCITT((short)11, (short)24), new CCITT((short)11, (short)24), new CCITT((short)11, (short)24), new CCITT((short)11, (short)24),		// 00000010111xx
				new CCITT((short)11, (short)25), new CCITT((short)11, (short)25), new CCITT((short)11, (short)25), new CCITT((short)11, (short)25),		// 00000011000xx
				new CCITT((short)13, (short)1664),						// 0000001100100
				new CCITT((short)13, (short)1728),						// 0000001100101
				new CCITT((short)12, (short)320), new CCITT((short)12, (short)320),					// 000000110011x
				new CCITT((short)12, (short)384), new CCITT((short)12, (short)384),					// 000000110100x
				new CCITT((short)12, (short)448), new CCITT((short)12, (short)448),					// 000000110101x
				new CCITT((short)13, (short)512),						// 0000001101100
				new CCITT((short)13, (short)576),						// 0000001101101
				new CCITT((short)12, (short)53), new CCITT((short)12, (short)53),					// 000000110111x
				new CCITT((short)12, (short)54), new CCITT((short)12, (short)54),					// 000000111000x
				new CCITT((short)13, (short)896),						// 0000001110010
				new CCITT((short)13, (short)960),						// 0000001110011
				new CCITT((short)13, (short)1024),						// 0000001110100
				new CCITT((short)13, (short)1088),						// 0000001110101
				new CCITT((short)13, (short)1152),						// 0000001110110
				new CCITT((short)13, (short)1216),						// 0000001110111
				new CCITT((short)10, (short)64), new CCITT((short)10, (short)64), new CCITT((short)10, (short)64), new CCITT((short)10, (short)64),		// 0000001111xxx
				new CCITT((short)10, (short)64), new CCITT((short)10, (short)64), new CCITT((short)10, (short)64), new CCITT((short)10, (short)64)
		};

//		7-12 bit codes (upper 4 bits are 0)
		CCITT blackTab2[] = {
				new CCITT((short)8, (short)13), new CCITT((short)8, (short)13), new CCITT((short)8, (short)13), new CCITT((short)8, (short)13),			// 00000100xxxx
				new CCITT((short)8, (short)13), new CCITT((short)8, (short)13), new CCITT((short)8, (short)13), new CCITT((short)8, (short)13),
				new CCITT((short)8, (short)13), new CCITT((short)8, (short)13), new CCITT((short)8, (short)13), new CCITT((short)8, (short)13),
				new CCITT((short)8, (short)13), new CCITT((short)8, (short)13), new CCITT((short)8, (short)13), new CCITT((short)8, (short)13),
				new CCITT((short)11, (short)23), new CCITT((short)11, (short)23),					// 00000101000x
				new CCITT((short)12, (short)50),						// 000001010010
				new CCITT((short)12, (short)51),						// 000001010011
				new CCITT((short)12, (short)44),						// 000001010100
				new CCITT((short)12, (short)45),						// 000001010101
				new CCITT((short)12, (short)46),						// 000001010110
				new CCITT((short)12, (short)47),						// 000001010111
				new CCITT((short)12, (short)57),						// 000001011000
				new CCITT((short)12, (short)58),						// 000001011001
				new CCITT((short)12, (short)61),						// 000001011010
				new CCITT((short)12, (short)256),						// 000001011011
				new CCITT((short)10, (short)16), new CCITT((short)10, (short)16), new CCITT((short)10, (short)16), new CCITT((short)10, (short)16),		// 0000010111xx
				new CCITT((short)10, (short)17), new CCITT((short)10, (short)17), new CCITT((short)10, (short)17), new CCITT((short)10, (short)17),		// 0000011000xx
				new CCITT((short)12, (short)48),						// 000001100100
				new CCITT((short)12, (short)49),						// 000001100101
				new CCITT((short)12, (short)62),						// 000001100110
				new CCITT((short)12, (short)63),						// 000001100111
				new CCITT((short)12, (short)30),						// 000001101000
				new CCITT((short)12, (short)31),						// 000001101001
				new CCITT((short)12, (short)32),						// 000001101010
				new CCITT((short)12, (short)33),						// 000001101011
				new CCITT((short)12, (short)40),						// 000001101100
				new CCITT((short)12, (short)41),						// 000001101101
				new CCITT((short)11, (short)22), new CCITT((short)11, (short)22),					// 00000110111x
				new CCITT((short)8, (short)14), new CCITT((short)8, (short)14), new CCITT((short)8, (short)14), new CCITT((short)8, (short)14),			// 00000111xxxx
				new CCITT((short)8, (short)14), new CCITT((short)8, (short)14), new CCITT((short)8, (short)14), new CCITT((short)8, (short)14),
				new CCITT((short)8, (short)14), new CCITT((short)8, (short)14), new CCITT((short)8, (short)14), new CCITT((short)8, (short)14),
				new CCITT((short)8, (short)14), new CCITT((short)8, (short)14), new CCITT((short)8, (short)14), new CCITT((short)8, (short)14),
				new CCITT((short)7, (short)10), new CCITT((short)7, (short)10), new CCITT((short)7, (short)10), new CCITT((short)7, (short)10),			// 0000100xxxxx
				new CCITT((short)7, (short)10), new CCITT((short)7, (short)10), new CCITT((short)7, (short)10), new CCITT((short)7, (short)10),
				new CCITT((short)7, (short)10), new CCITT((short)7, (short)10), new CCITT((short)7, (short)10), new CCITT((short)7, (short)10),
				new CCITT((short)7, (short)10), new CCITT((short)7, (short)10), new CCITT((short)7, (short)10), new CCITT((short)7, (short)10),
				new CCITT((short)7, (short)10), new CCITT((short)7, (short)10), new CCITT((short)7, (short)10), new CCITT((short)7, (short)10),
				new CCITT((short)7, (short)10), new CCITT((short)7, (short)10), new CCITT((short)7, (short)10), new CCITT((short)7, (short)10),
				new CCITT((short)7, (short)10), new CCITT((short)7, (short)10), new CCITT((short)7, (short)10), new CCITT((short)7, (short)10),
				new CCITT((short)7, (short)10), new CCITT((short)7, (short)10), new CCITT((short)7, (short)10), new CCITT((short)7, (short)10),
				new CCITT((short)7, (short)11), new CCITT((short)7, (short)11), new CCITT((short)7, (short)11), new CCITT((short)7, (short)11),			// 0000101xxxxx
				new CCITT((short)7, (short)11), new CCITT((short)7, (short)11), new CCITT((short)7, (short)11), new CCITT((short)7, (short)11),
				new CCITT((short)7, (short)11), new CCITT((short)7, (short)11), new CCITT((short)7, (short)11), new CCITT((short)7, (short)11),
				new CCITT((short)7, (short)11), new CCITT((short)7, (short)11), new CCITT((short)7, (short)11), new CCITT((short)7, (short)11),
				new CCITT((short)7, (short)11), new CCITT((short)7, (short)11), new CCITT((short)7, (short)11), new CCITT((short)7, (short)11),
				new CCITT((short)7, (short)11), new CCITT((short)7, (short)11), new CCITT((short)7, (short)11), new CCITT((short)7, (short)11),
				new CCITT((short)7, (short)11), new CCITT((short)7, (short)11), new CCITT((short)7, (short)11), new CCITT((short)7, (short)11),
				new CCITT((short)7, (short)11), new CCITT((short)7, (short)11), new CCITT((short)7, (short)11), new CCITT((short)7, (short)11),
				new CCITT((short)9, (short)15), new CCITT((short)9, (short)15), new CCITT((short)9, (short)15), new CCITT((short)9, (short)15),			// 000011000xxx
				new CCITT((short)9, (short)15), new CCITT((short)9, (short)15), new CCITT((short)9, (short)15), new CCITT((short)9, (short)15),
				new CCITT((short)12, (short)128),						// 000011001000
				new CCITT((short)12, (short)192),						// 000011001001
				new CCITT((short)12, (short)26),						// 000011001010
				new CCITT((short)12, (short)27),						// 000011001011
				new CCITT((short)12, (short)28),						// 000011001100
				new CCITT((short)12, (short)29),						// 000011001101
				new CCITT((short)11, (short)19), new CCITT((short)11, (short)19),					// 00001100111x
				new CCITT((short)11, (short)20), new CCITT((short)11, (short)20),					// 00001101000x
				new CCITT((short)12, (short)34),						// 000011010010
				new CCITT((short)12, (short)35),						// 000011010011
				new CCITT((short)12, (short)36),						// 000011010100
				new CCITT((short)12, (short)37),						// 000011010101
				new CCITT((short)12, (short)38),						// 000011010110
				new CCITT((short)12, (short)39),						// 000011010111
				new CCITT((short)11, (short)21), new CCITT((short)11, (short)21),					// 00001101100x
				new CCITT((short)12, (short)42),						// 000011011010
				new CCITT((short)12, (short)43),						// 000011011011
				new CCITT((short)10, (short)0), new CCITT((short)10, (short)0), new CCITT((short)10, (short)0), new CCITT((short)10, (short)0),			// 0000110111xx
				new CCITT((short)7, (short)12), new CCITT((short)7, (short)12), new CCITT((short)7, (short)12), new CCITT((short)7, (short)12),			// 0000111xxxxx
				new CCITT((short)7, (short)12), new CCITT((short)7, (short)12), new CCITT((short)7, (short)12), new CCITT((short)7, (short)12),
				new CCITT((short)7, (short)12), new CCITT((short)7, (short)12), new CCITT((short)7, (short)12), new CCITT((short)7, (short)12),
				new CCITT((short)7, (short)12), new CCITT((short)7, (short)12), new CCITT((short)7, (short)12), new CCITT((short)7, (short)12),
				new CCITT((short)7, (short)12), new CCITT((short)7, (short)12), new CCITT((short)7, (short)12), new CCITT((short)7, (short)12),
				new CCITT((short)7, (short)12), new CCITT((short)7, (short)12), new CCITT((short)7, (short)12), new CCITT((short)7, (short)12),
				new CCITT((short)7, (short)12), new CCITT((short)7, (short)12), new CCITT((short)7, (short)12), new CCITT((short)7, (short)12),
				new CCITT((short)7, (short)12), new CCITT((short)7, (short)12), new CCITT((short)7, (short)12), new CCITT((short)7, (short)12)
		};

//		2-6 bit codes
		CCITT blackTab3[] = {
				new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1), new CCITT((short)-1, (short)-1),		// 0000xx
				new CCITT((short)6, (short)9),						// 000100
				new CCITT((short)6, (short)8),						// 000101
				new CCITT((short)5, (short)7), new CCITT((short)5, (short)7),					// 00011x
				new CCITT((short)4, (short)6), new CCITT((short)4, (short)6), new CCITT((short)4, (short)6), new CCITT((short)4, (short)6),			// 0010xx
				new CCITT((short)4, (short)5), new CCITT((short)4, (short)5), new CCITT((short)4, (short)5), new CCITT((short)4, (short)5),			// 0011xx
				new CCITT((short)3, (short)1), new CCITT((short)3, (short)1), new CCITT((short)3, (short)1), new CCITT((short)3, (short)1),			// 010xxx
				new CCITT((short)3, (short)1), new CCITT((short)3, (short)1), new CCITT((short)3, (short)1), new CCITT((short)3, (short)1),
				new CCITT((short)3, (short)4), new CCITT((short)3, (short)4), new CCITT((short)3, (short)4), new CCITT((short)3, (short)4),			// 011xxx
				new CCITT((short)3, (short)4), new CCITT((short)3, (short)4), new CCITT((short)3, (short)4), new CCITT((short)3, (short)4),
				new CCITT((short)2, (short)3), new CCITT((short)2, (short)3), new CCITT((short)2, (short)3), new CCITT((short)2, (short)3),			// 10xxxx
				new CCITT((short)2, (short)3), new CCITT((short)2, (short)3), new CCITT((short)2, (short)3), new CCITT((short)2, (short)3),
				new CCITT((short)2, (short)3), new CCITT((short)2, (short)3), new CCITT((short)2, (short)3), new CCITT((short)2, (short)3),
				new CCITT((short)2, (short)3), new CCITT((short)2, (short)3), new CCITT((short)2, (short)3), new CCITT((short)2, (short)3),
				new CCITT((short)2, (short)2), new CCITT((short)2, (short)2), new CCITT((short)2, (short)2), new CCITT((short)2, (short)2),			// 11xxxx
				new CCITT((short)2, (short)2), new CCITT((short)2, (short)2), new CCITT((short)2, (short)2), new CCITT((short)2, (short)2),
				new CCITT((short)2, (short)2), new CCITT((short)2, (short)2), new CCITT((short)2, (short)2), new CCITT((short)2, (short)2),
				new CCITT((short)2, (short)2), new CCITT((short)2, (short)2), new CCITT((short)2, (short)2), new CCITT((short)2, (short)2)
		};
	}
	public short getTwoDimHoriz() {
		return twoDimHoriz;
	}

	public short getTwoDimPass() {
		return twoDimPass;
	}

	public CCITT[] getTwoDimTab1() {
		return twoDimTab1;
	}

	public short getTwoDimVert0() {
		return twoDimVert0;
	}

	public short getTwoDimVertL1() {
		return twoDimVertL1;
	}

	public short getTwoDimVertL2() {
		return twoDimVertL2;
	}

	public short getTwoDimVertL3() {
		return twoDimVertL3;
	}

	public short getTwoDimVertR1() {
		return twoDimVertR1;
	}

	public short getTwoDimVertR2() {
		return twoDimVertR2;
	}

	public short getTwoDimVertR3() {
		return twoDimVertR3;
	}

	public CCITT[] getBlackTab1() {
		return blackTab1;
	}

	public CCITT[] getBlackTab2() {
		return blackTab2;
	}

	public CCITT[] getBlackTab3() {
		return blackTab3;
	}

	public short getCcittEOL() {
		return ccittEOL;
	}

	public CCITT[] getWhiteTab1() {
		return whiteTab1;
	}

	public CCITT[] getWhiteTab2() {
		return whiteTab2;
	}

	public void setTwoDimTab1(CCITT[] twoDimTab1) {
		this.twoDimTab1 = twoDimTab1;
	}
}
