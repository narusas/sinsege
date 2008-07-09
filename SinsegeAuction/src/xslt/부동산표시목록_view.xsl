<?xml version="1.0" encoding="euc-kr"?>
<!DOCTYPE 부동산표시목록
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
					[&nbsp;<xsl:value-of select="부동산표시목록/@사건번호" />&nbsp;]
				
			</td>
		</tr>	
	</table>
	
    		<table align="center" border="1" width="95%" cellpadding="10" cellspacing="0">
    			<thead>
    			
    			<tr>
    				<td colspan="4"><p align="center"><font size="5"><b>부 동 산 표 시 목 록</b></font></p>
    				</td>
    			</tr>
    			
    			
    			<tr>
						<td width="7%"><p align="center"><font size="3"><b>번호</b></font></p></td>
						<td width="28%"><p align="center"><font size="3"><b>지   번</b></font></p></td>
						<td width="40%"><p align="center"><font size="3"><b>용  도/ 구  조/ 면 적</b></font></p></td>
						<td width="25%"><p align="center"><font size="3"><b>비    고</b></font></p></td>
			</tr>
			</thead>
			<tbody>
			<xsl:for-each select="부동산표시목록/부동산정보">
			<tr>
			  		<td ><p align="center"><font size="2"><xsl:value-of select="@일련번호" /></font></p></td>
			  		<td ><p align="left"><font size="2"><xsl:value-of select="주소/시도" />&nbsp;<xsl:value-of select="주소/시군구" />&nbsp;<xsl:value-of select="주소/읍면동" />&nbsp;<xsl:value-of select="주소/번지이하" /></font></p></td>
			   		<td><pre align="left"><font size="2">&nbsp;<xsl:value-of select="용도구조면적" /></font></pre></td>
			  		<xsl:choose>
			  			<xsl:when test="비고">
			  				<td ><p align="left"><font size="2">&nbsp;<xsl:value-of select="비고" /></font></p></td>
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
    				<td colspan="4"><p align="right"><font size="3"><xsl:value-of select="부동산표시목록/@사건명" /></font></p>
    				</td>
    			</tr>
			</tfoot>
		</table>
<!--		<table align="center" border="0" width="95%">
			<tr>
				<td  align="right">
					
						<xsl:value-of select="부동산표시목록/@사건명" />
					
				</td>
			</tr>
		</table>
		 -->
	</body>
    	</html>
</xsl:template>

</xsl:stylesheet>
