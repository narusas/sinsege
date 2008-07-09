<?xml version="1.0" encoding="euc-kr"?>
<!DOCTYPE ��Ȳ����������
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
					[ <xsl:value-of select="��Ȳ����������/@��ǹ�ȣ" /> ]
				
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
		<td colspan="8"><p align="center"><font size="5"><b>�ε����� ��Ȳ �� �������� ���缭</b></font></p></td>
	</tr>
	</thead>
	<tr>
		<td>

						<b>1. �ε����� ��������</b>
					

			<xsl:for-each select="��Ȳ����������/���纰" >
			<table align="center" border="1" width="95%"  cellpadding="10" cellspacing="0">
			<tbody>
						
						<tr>
							<td width="20%" align="center"><p>������</p></td>
							<td width="80%" >&nbsp;<xsl:value-of select="@�Ϸù�ȣ" />.&nbsp;<xsl:value-of select="@�õ�" />&nbsp;<xsl:value-of select="@�ñ���" />&nbsp;<xsl:value-of select="@���鵿" />&nbsp;<xsl:value-of select="@��������" />&nbsp;</td>
							
						</tr>
						
						<tr>
							<td  width="20%" align="center"><p>��������</p></td>
							<xsl:choose>
									<xsl:when test="��������/@���������ڵ�[.='01']">
										
											<td align="left"><p>&nbsp;ä����(������)����</p></td>
										
									</xsl:when>
									<xsl:when test="��������/@���������ڵ�[.='02']">
										
											<td align="left"><p>&nbsp;������(����)����</p></td>
										
									</xsl:when>
									<xsl:when test="��������/@���������ڵ�[.='03']">
										
											<td align="left"><p>&nbsp;��3������ </p></td>
									</xsl:when>
									<xsl:when test="��������/@���������ڵ�[.='04']">
										
											<td align="left"><p>&nbsp;ä����(������)����,&nbsp;������(����)����,&nbsp;��3������ </p></td>
									</xsl:when>
									<xsl:when test="��������/@���������ڵ�[.='05']">
										
											<td align="left"><p>&nbsp;ä����(������)����,&nbsp;������(����)����</p></td>
									</xsl:when>
									<xsl:when test="��������/@���������ڵ�[.='06']">
										
											<td align="left"><p>&nbsp;������(����)����,&nbsp;��3������ </p></td>
									</xsl:when>
									<xsl:when test="��������/@���������ڵ�[.='07']">
										
											<td align="left"><p>&nbsp;ä����(������)����,&nbsp;��3������ </p></td>
									</xsl:when>
									<xsl:when test="��������/@���������ڵ�[.='08']">
										
											<td align="left"><p>&nbsp;</p></td>
									</xsl:when>
									<xsl:when test="��������/@���������ڵ�[.='09']">
										
											<td align="left"><p>&nbsp;�̻�</p></td>
									</xsl:when>
								</xsl:choose>
						</tr>
						<tr>
							<td  width="20%" align="center"><p>�� Ÿ</p></td>
							
								<xsl:choose>
									<xsl:when test="��Ÿ[.!='']">
										<td><p><xsl:value-of select="��Ÿ" /></p></td>
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
					<xsl:when test="/��Ȳ����������/���[.!='']">


									<b>2. �ε����� ��Ȳ</b>


							<pre><font size="3"><xsl:value-of select="��Ȳ����������/���" /></font></pre>
							

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
