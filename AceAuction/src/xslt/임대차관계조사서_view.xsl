<?xml version="1.0" encoding="euc-kr"?>

<!DOCTYPE �Ӵ����������缭
[ 
        <!ENTITY  nbsp  "&#160;">
	<!ENTITY  divide  "&#218;">
]>



<xsl:stylesheet 
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
 <!-- 

 �� XML XSL �� ���ѹα� ������� ��� ���� ����ȭ ������Ʈ��
 "�Ӵ��� ���� ���缭"�� ���� ���̴�.
  
 ��     �� : ��â�� (ChangOh Jung)
 COTECH,Inc. R&D Center
 
 * History *
 2000.10.09 : create
 2000.10.10 : DTD ������ ���� �� ����.
 2000.10.12 : �� �������� �ϳ��� ���纰 ���븸 �����ϵ��� �籸��.
 2000.11.01 : ����� DTD�� ���� ǥ�� ����� ����.
 2000.11.22 : �ֹε�Ϲ�ȣ ���� ��
 
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
					[ <xsl:value-of select="�Ӵ����������缭/@��ǹ�ȣ" /> ]
				
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
			<td colspan="8"><p align="center"><font size="5"><b>�� �� �� �� �� �� �� ��</b></font></p></td>
		</tr>
		</thead>
		<tr>
			<td>


							<b>1. ���� �������� �뵵 �� �Ӵ��� ������ ����</b>
							<br/>
							<br/>

				<tbody>	

				<xsl:for-each select="�Ӵ����������缭/���纰" >
				
				<table align="center" border="1" width="90%"  cellpadding="10" cellspacing="0">

							
							<tr>
								<td class="unnamed1" width="100%"  align="left" colspan="5">[ ������ ]&nbsp;<xsl:value-of select="@�Ϸù�ȣ" />.&nbsp;<xsl:value-of select="@�õ�" />&nbsp;<xsl:value-of select="@�ñ���" />&nbsp;<xsl:value-of select="@���鵿" />&nbsp;<xsl:value-of select="@��������" />&nbsp;</td>
							</tr>
							<xsl:for-each select="��������" >
		<div id="print">
				<table align="center" width="90%" border="1" cellspacing="0" cellpadding="0">
							<tr>
						 
							<tr>
								<td width="4%" height="100" align="center" rowspan="5">&nbsp;<xsl:value-of select="@����" /></td>
								<td width="24%" height="38" align="center" >������</td>
								<td width="24%" height="38" align="left" >&nbsp;<xsl:value-of select="������" /></td>
								<td width="24%" height="38" align="center" >����ڱ���</td>
								<td width="24%" height="38" align="left" >&nbsp;<xsl:value-of select="������/@����" /></td>
							</tr>
					 
							<tr>
								<td align="center" height="38" >�����κ�</td>
								<td align="left" height="38" >&nbsp;<xsl:value-of select="�����κ�" /></td>
								<td align="center" height="38" >�뵵</td>
								<td align="left" height="38" >&nbsp;<xsl:value-of select="�뵵" />
								<xsl:choose>
									<xsl:when test="��Ÿ��[.!='']">
										- <xsl:value-of select="��Ÿ��" />
									</xsl:when>
									<xsl:otherwise>&nbsp;</xsl:otherwise>
								</xsl:choose>								
								</td>


							</tr>
							<tr>
								<td align="center" height="38" >�����Ⱓ</td>
								<xsl:choose>
									<xsl:when test="�����Ⱓ">
										<td colspan="3" align="left" height="38" ><p>&nbsp;<xsl:value-of select="�����Ⱓ" /></p></td>
									</xsl:when>
									<xsl:otherwise>
						      			<td colspan="3" height="38" ><p align="left">&nbsp;-</p></td>
						     			</xsl:otherwise>
							      	</xsl:choose>
								
							</tr>   
							<tr>
								<td align="center" height="38" >����(����)��</td>
								<xsl:choose>
									<xsl:when test="������">
										<td class="unnamed1" align="left" height="38" ><p>&nbsp;<xsl:value-of select="������" />&nbsp;</p></td>
									</xsl:when>
									<xsl:otherwise>
							      			<td class="unnamed1" align="left" height="38" ><p>&nbsp;-</p></td>
							     	  	</xsl:otherwise>
							      	</xsl:choose>
							      	<td align="center" height="38" >����</td>
								<xsl:choose>
									<xsl:when test="����">
										<td class="unnamed1" align="left" height="38" ><p>&nbsp;<xsl:value-of select="����" />&nbsp;</p></td>
									</xsl:when>
									<xsl:otherwise>
							      			<td class="unnamed1" align="left" height="38" ><p>&nbsp;-</p></td>
							     	  	</xsl:otherwise>
							      	</xsl:choose>
								
							</tr>
							<tr>
								<td align="center" height="38" >��������</td>
								<xsl:choose>
									<xsl:when test="��������/@����[.='����']">
										<td class="unnamed1" height="38" ><p align="left">&nbsp;<xsl:value-of select="��������" /></p></td>
									</xsl:when>
									<xsl:when test="��������/@����[.='�̻�']">
										<td class="unnamed1" height="38" ><p align="left">&nbsp;�� ��</p></td>
									</xsl:when>
									<xsl:when test="��������/@����[.='����']">
										<td class="unnamed1" height="38" ><p align="left">&nbsp;</p></td>
									</xsl:when>
									<xsl:otherwise>
										<td class="unnamed1" height="38" ><p align="left">&nbsp;-</p></td>
									</xsl:otherwise>
								</xsl:choose> 
							      	<td align="center" height="38" >Ȯ������</td>
								<xsl:choose>
									<xsl:when test="Ȯ������/@����[.='����']">
										<td class="unnamed1" height="38" ><p align="left">&nbsp;<xsl:value-of select="Ȯ������" /></p></td>
									</xsl:when>
									<xsl:when test="Ȯ������/@����[.='�̻�']">
										<td class="unnamed1" height="38" ><p align="left">&nbsp;�� ��</p></td>
									</xsl:when>
									<xsl:when test="Ȯ������/@����[.='����']">
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
					<xsl:when test="/�Ӵ����������缭/��Ÿ[.!='']">
						<tr>

									<b>2. �� Ÿ</b>
								

						</tr>
						<tr>
							<td><pre><font size="3"><xsl:value-of select="�Ӵ����������缭/��Ÿ" /></font></pre>
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
