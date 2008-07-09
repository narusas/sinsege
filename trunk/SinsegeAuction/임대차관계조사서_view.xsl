<?xml version="1.0" encoding="euc-kr"?>

<!DOCTYPE 임대차관계조사서
[ 
        <!ENTITY  nbsp  "&#160;">
	<!ENTITY  divide  "&#218;">
]>



<xsl:stylesheet 
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
 <!-- 

 이 XML XSL 은 대한민국 대법원의 경매 정보 전산화 프로젝트의
 "임대차 관계 조사서"를 위한 것이다.
  
 작     성 : 정창오 (ChangOh Jung)
 COTECH,Inc. R&D Center
 
 * History *
 2000.10.09 : create
 2000.10.10 : DTD 수정에 따른 재 구성.
 2000.10.12 : 한 페이지당 하나의 소재별 내용만 포함하도록 재구성.
 2000.11.01 : 변경된 DTD에 따라 표현 양식을 수정.
 2000.11.22 : 주민등록번호 삭제 등
 
-->
        
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
	<SCRIPT language="JavaScript" implements-prefix="user"><xsl:comment><![CDATA[
	  function setPrint() {
	  var coll = document.all.tags("div");
	 	for (i=0; i<coll.length; i++) {  
		 
			if(i%4==0&&i!=0){
				coll(i).style.pageBreakBefore = "always";
			}
		}
		// window.print();
		 
	  }

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
					[ <xsl:value-of select="임대차관계조사서/@사건번호" /> ]
				
			</td>
		</tr>	
	</table>
    	</head>
    	<body onunload="goBack();">
<table align="center" border="0" width="95%"  cellpadding="10" cellspacing="0">
	<tr>
	<td>
	<!--
	<table width="570" border="0" cellspacing="0" cellpadding="0" bordercolor="#191C16" bordercolordark="#FFFFFF" height="5" align="center">
	-->
		<thead>
		<tr>
			<td colspan="8"><p align="center"><font size="5"><b>임 대 차 관 계 조 사 서</b></font></p></td>
		</tr>
		</thead>
		<tr>
			<td>


							<b>1. 임차 목적물의 용도 및 임대차 계약등의 내용</b>
							<br/>
							<br/>

				<tbody>	

				<xsl:for-each select="임대차관계조사서/소재별" >
				
				<table align="center" border="1" width="90%"  cellpadding="10" cellspacing="0">

							
							<tr>
								<td class="unnamed1" width="100%"  align="left" colspan="5">[ 소재지 ]&nbsp;<xsl:value-of select="@일련번호" />.&nbsp;<xsl:value-of select="@시도" />&nbsp;<xsl:value-of select="@시군구" />&nbsp;<xsl:value-of select="@읍면동" />&nbsp;<xsl:value-of select="@번지이하" />&nbsp;</td>
							</tr>
							<xsl:for-each select="임차내용" >
		<div id="print">
				<table align="center" width="90%" border="1" cellspacing="0" cellpadding="0">
							<tr>
						 
							<tr>
								<td width="4%" height="100" align="center" rowspan="5">&nbsp;<xsl:value-of select="@차례" /></td>
								<td width="24%" height="38" align="center" >점유인</td>
								<td width="24%" height="38" align="left" >&nbsp;<xsl:value-of select="임차인" /></td>
								<td width="24%" height="38" align="center" >당사자구분</td>
								<td width="24%" height="38" align="left" >&nbsp;<xsl:value-of select="임차인/@구분" /></td>
							</tr>
					 
							<tr>
								<td align="center" height="38" >점유부분</td>
								<td align="left" height="38" >&nbsp;<xsl:value-of select="임차부분" /></td>
								<td align="center" height="38" >용도</td>
								<td align="left" height="38" >&nbsp;<xsl:value-of select="용도" />
								<xsl:choose>
									<xsl:when test="기타란[.!='']">
										- <xsl:value-of select="기타란" />
									</xsl:when>
									<xsl:otherwise>&nbsp;</xsl:otherwise>
								</xsl:choose>								
								</td>


							</tr>
							<tr>
								<td align="center" height="38" >점유기간</td>
								<xsl:choose>
									<xsl:when test="임차기간">
										<td colspan="3" align="left" height="38" ><p>&nbsp;<xsl:value-of select="임차기간" /></p></td>
									</xsl:when>
									<xsl:otherwise>
						      			<td colspan="3" height="38" ><p align="left">&nbsp;-</p></td>
						     			</xsl:otherwise>
							      	</xsl:choose>
								
							</tr>   
							<tr>
								<td align="center" height="38" >보증(전세)금</td>
								<xsl:choose>
									<xsl:when test="보증금">
										<td class="unnamed1" align="left" height="38" ><p>&nbsp;<xsl:value-of select="보증금" />&nbsp;</p></td>
									</xsl:when>
									<xsl:otherwise>
							      			<td class="unnamed1" align="left" height="38" ><p>&nbsp;-</p></td>
							     	  	</xsl:otherwise>
							      	</xsl:choose>
							      	<td align="center" height="38" >차임</td>
								<xsl:choose>
									<xsl:when test="차임">
										<td class="unnamed1" align="left" height="38" ><p>&nbsp;<xsl:value-of select="차임" />&nbsp;</p></td>
									</xsl:when>
									<xsl:otherwise>
							      			<td class="unnamed1" align="left" height="38" ><p>&nbsp;-</p></td>
							     	  	</xsl:otherwise>
							      	</xsl:choose>
								
							</tr>
							<tr>
								<td align="center" height="38" >전입일자</td>
								<xsl:choose>
									<xsl:when test="전입일자/@유무[.='있음']">
										<td class="unnamed1" height="38" ><p align="left">&nbsp;<xsl:value-of select="전입일자" /></p></td>
									</xsl:when>
									<xsl:when test="전입일자/@유무[.='미상']">
										<td class="unnamed1" height="38" ><p align="left">&nbsp;미 상</p></td>
									</xsl:when>
									<xsl:when test="전입일자/@유무[.='없음']">
										<td class="unnamed1" height="38" ><p align="left">&nbsp;</p></td>
									</xsl:when>
									<xsl:otherwise>
										<td class="unnamed1" height="38" ><p align="left">&nbsp;-</p></td>
									</xsl:otherwise>
								</xsl:choose> 
							      	<td align="center" height="38" >확정일자</td>
								<xsl:choose>
									<xsl:when test="확정일자/@유무[.='있음']">
										<td class="unnamed1" height="38" ><p align="left">&nbsp;<xsl:value-of select="확정일자" /></p></td>
									</xsl:when>
									<xsl:when test="확정일자/@유무[.='미상']">
										<td class="unnamed1" height="38" ><p align="left">&nbsp;미 상</p></td>
									</xsl:when>
									<xsl:when test="확정일자/@유무[.='없음']">
										<td class="unnamed1" height="38" ><p align="left">&nbsp;</p></td>
									</xsl:when>
									<xsl:otherwise>
										<td class="unnamed1" height="38" ><p align="left">&nbsp;-</p></td>
									</xsl:otherwise>
								</xsl:choose>
							</tr>
						 
							</tr>

							</table>
							</div>
							</xsl:for-each>


				</table>
				
				</xsl:for-each>
				</tbody>
				
				<br/>
				<table align="center" border="0" width="93%"  cellpadding="10" cellspacing="0">
				<xsl:choose>
					<xsl:when test="/임대차관계조사서/기타[.!='']">
						<tr>

									<b>2. 기 타</b>
								

						</tr>
						<tr>
							<td><pre><font size="3"><xsl:value-of select="임대차관계조사서/기타" /></font></pre>
							</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td><p>&nbsp;</p></td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>			
				</table>
				
			</td>
		</tr>
	</td>
	</tr>
	<tfoot>
	<tr>
		<td colspan="4"><p align="right"><font size="2"></font></p>
		</td>
	</tr>
	</tfoot>

</table>
</body>
</html>
</xsl:template>]
<xsl:script> 
<![CDATA[ 
	   count = 0; 
	   count1 = 0;
	   total_amount = 0;
	 
	    function count_loop1() {
	      count1++;
	      if(count1%3==0)
	      {
	      
	      return "aaa";
	      }
	      else{
	          
	      }
	   } 
]]> 
  </xsl:script>
</xsl:stylesheet>
