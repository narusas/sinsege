<?xml version="1.0" encoding="euc-kr"?>
<!DOCTYPE 현황및점유관계
  [
  <!ENTITY  nbsp	"&#160;">
  ]
>
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
	<table align="center" border="0" width="95%">
		<tr>
			<td align="left">
					[ <xsl:value-of select="현황및점유관계/@사건번호" /> ]
				
			</td>
		</tr>	
	</table>

</head>

<body onunload="goBack();">
<table align="center" border="0" width="95%"  cellpadding="10" cellspacing="0">
	<tr>
	<td>
	<thead>
	<tr>
		<td colspan="8"><p align="center"><font size="5"><b>부동산의 현황 및 점유관계 조사서</b></font></p></td>
	</tr>
	</thead>
	<tr>
		<td>

						<b>1. 부동산의 점유관계</b>
					

			<xsl:for-each select="현황및점유관계/소재별" >
			<table align="center" border="1" width="95%"  cellpadding="10" cellspacing="0">
			<tbody>
						
						<tr>
							<td width="20%" align="center"><p>소재지</p></td>
							<td width="80%" >&nbsp;<xsl:value-of select="@일련번호" />.&nbsp;<xsl:value-of select="@시도" />&nbsp;<xsl:value-of select="@시군구" />&nbsp;<xsl:value-of select="@읍면동" />&nbsp;<xsl:value-of select="@번지이하" />&nbsp;</td>
							
						</tr>
						
						<tr>
							<td  width="20%" align="center"><p>점유관계</p></td>
							<xsl:choose>
									<xsl:when test="점유관계/@점유관계코드[.='01']">
										
											<td align="left"><p>&nbsp;채무자(소유자)점유</p></td>
										
									</xsl:when>
									<xsl:when test="점유관계/@점유관계코드[.='02']">
										
											<td align="left"><p>&nbsp;임차인(별지)점유</p></td>
										
									</xsl:when>
									<xsl:when test="점유관계/@점유관계코드[.='03']">
										
											<td align="left"><p>&nbsp;제3자점유 </p></td>
									</xsl:when>
									<xsl:when test="점유관계/@점유관계코드[.='04']">
										
											<td align="left"><p>&nbsp;채무자(소유자)점유,&nbsp;임차인(별지)점유,&nbsp;제3자점유 </p></td>
									</xsl:when>
									<xsl:when test="점유관계/@점유관계코드[.='05']">
										
											<td align="left"><p>&nbsp;채무자(소유자)점유,&nbsp;임차인(별지)점유</p></td>
									</xsl:when>
									<xsl:when test="점유관계/@점유관계코드[.='06']">
										
											<td align="left"><p>&nbsp;임차인(별지)점유,&nbsp;제3자점유 </p></td>
									</xsl:when>
									<xsl:when test="점유관계/@점유관계코드[.='07']">
										
											<td align="left"><p>&nbsp;채무자(소유자)점유,&nbsp;제3자점유 </p></td>
									</xsl:when>
									<xsl:when test="점유관계/@점유관계코드[.='08']">
										
											<td align="left"><p>&nbsp;</p></td>
									</xsl:when>
									<xsl:when test="점유관계/@점유관계코드[.='09']">
										
											<td align="left"><p>&nbsp;미상</p></td>
									</xsl:when>
								</xsl:choose>
						</tr>
						<tr>
							<td  width="20%" align="center"><p>기 타</p></td>
							
								<xsl:choose>
									<xsl:when test="기타[.!='']">
										<td><p><xsl:value-of select="기타" /></p></td>
									</xsl:when>
									<xsl:otherwise>
							      			<td><p>&nbsp;</p></td>
							     	</xsl:otherwise>
								</xsl:choose>
							
						</tr>
				
			<br/>
			</tbody>
			</table>
			</xsl:for-each>
			<br/>

			<xsl:choose>
					<xsl:when test="/현황및점유관계/비고[.!='']">


									<b>2. 부동산의 현황</b>


							<pre><font size="3"><xsl:value-of select="현황및점유관계/비고" /></font></pre>
							

					</xsl:when>
					<xsl:otherwise>

							<p align="left">&nbsp;</p>

					</xsl:otherwise>
			</xsl:choose>				

		</td>
		</tr>
	</td>
	</tr>
	<tfoot>
<!--
	<tr>
		<td colspan="4"><p align="right"><font size="2"></font></p>
		</td>
	</tr>
-->
	</tfoot>
</table>
</body>
</html>

</xsl:template>

</xsl:stylesheet>
