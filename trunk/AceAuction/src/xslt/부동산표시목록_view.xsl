<?xml version="1.0" encoding="euc-kr"?>
<!DOCTYPE �ε���ǥ�ø��
[ 
        <!ENTITY  nbsp  "&#160;">
]>

<xsl:stylesheet 
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
	<html>
    	<head>
    	<STYLE>
	TABLE    { display: table }
	TR       { display: table-row }
	THEAD    { display: table-header-group }
	TBODY    { display: table-row-group }
	TFOOT    { display: table-footer-group }
	COL      { display: table-column }
	COLGROUP { display: table-column-group }
	TD, TH   { display: table-cell }
	CAPTION  { display: table-caption }
	DIV.page { page-break-before: always }
	</STYLE>

<SCRIPT language="JavaScript"><xsl:comment><![CDATA[

	function goBack()
	{
		var xCoord =0;
		var yCoord =0;

		if (document.layers){ 
			xCoord = e.x; 
			yCoord = e.y; 
		} 
		else if (document.all){ 
			xCoord = event.clientX; 
			yCoord = event.clientY; 
		} 
		else if (document.getElementById){ 
			xCoord = e.clientX; 
			yCoord = e.clientY; 
		}

		if( xCoord<0 && yCoord<0 ){ history.back(); }	
	}

]]></xsl:comment>
</SCRIPT>
</head>



<body onunload="goBack();">
	<table align="center" border="0" width="95%">
		<tr>
			<td align="left">
					[&nbsp;<xsl:value-of select="�ε���ǥ�ø��/@��ǹ�ȣ" />&nbsp;]
				
			</td>
		</tr>	
	</table>
	
    		<table align="center" border="1" width="95%" cellpadding="10" cellspacing="0">
    			<thead>
    			
    			<tr>
    				<td colspan="4"><p align="center"><font size="5"><b>�� �� �� ǥ �� �� ��</b></font></p>
    				</td>
    			</tr>
    			
    			
    			<tr>
						<td width="7%"><p align="center"><font size="3"><b>��ȣ</b></font></p></td>
						<td width="28%"><p align="center"><font size="3"><b>��   ��</b></font></p></td>
						<td width="40%"><p align="center"><font size="3"><b>��  ��/ ��  ��/ �� ��</b></font></p></td>
						<td width="25%"><p align="center"><font size="3"><b>��    ��</b></font></p></td>
			</tr>
			</thead>
			<tbody>
			<xsl:for-each select="�ε���ǥ�ø��/�ε�������">
			<tr>
			  		<td ><p align="center"><font size="2"><xsl:value-of select="@�Ϸù�ȣ" /></font></p></td>
			  		<td ><p align="left"><font size="2"><xsl:value-of select="�ּ�/�õ�" />&nbsp;<xsl:value-of select="�ּ�/�ñ���" />&nbsp;<xsl:value-of select="�ּ�/���鵿" />&nbsp;<xsl:value-of select="�ּ�/��������" /></font></p></td>
			   		<td><pre align="left"><font size="2">&nbsp;<xsl:value-of select="�뵵��������" /></font></pre></td>
			  		<xsl:choose>
			  			<xsl:when test="���">
			  				<td ><p align="left"><font size="2">&nbsp;<xsl:value-of select="���" /></font></p></td>
			  	      		</xsl:when>
			  	       		<xsl:otherwise>
			  	   			<td ><p align="center"><font size="2"> --- </font></p></td>
			  	      		</xsl:otherwise>
			  		</xsl:choose>
			</tr>
			</xsl:for-each>
			</tbody>
			
			<tfoot>
			<tr>
    				<td colspan="4"><p align="right"><font size="3"><xsl:value-of select="�ε���ǥ�ø��/@��Ǹ�" /></font></p>
    				</td>
    			</tr>
			</tfoot>
		</table>
<!--		<table align="center" border="0" width="95%">
			<tr>
				<td  align="right">
					
						<xsl:value-of select="�ε���ǥ�ø��/@��Ǹ�" />
					
				</td>
			</tr>
		</table>
		 -->
	</body>
    	</html>
</xsl:template>

</xsl:stylesheet>
