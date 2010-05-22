<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

	<xsl:template match="ResultsSession">
	
		<xsl:element name="checkstyle">	 
			<xsl:attribute name="xmlns_xsi">
				<xsl:text>http://www.w3.org/2001/XMLSchema-instance</xsl:text>
			</xsl:attribute>		
			<xsl:attribute name="version">
				<xsl:text>4.4</xsl:text>
			</xsl:attribute>
		
			<xsl:for-each-group select="CodingStandards/StdViols/StdViol" group-by="@locFile">
			
				<xsl:element name="file">
					<xsl:attribute name="name">
						<xsl:value-of select="@locFile"/>
					</xsl:attribute>
		
					<xsl:for-each select="current-group()">
				
						<xsl:element name="error">
							<xsl:attribute name="line">
								<xsl:value-of select="@ln"/>
							</xsl:attribute>
							<xsl:attribute name="column">
								<xsl:value-of select="@AD"/>
							</xsl:attribute>
							<xsl:attribute name="severity">
								<!-- Les conditions ci-dessous peuvent être modifiées pour trier les niveaux info, warning et error qui dépendent du choix de l'utilisateur-->
								<xsl:if test="(@sev=0)">
							    	<xsl:text>error</xsl:text>
							  	</xsl:if>
								<xsl:if test="(@sev=1)or(@sev=2)or((@sev=3)and(@urgent))">
									<xsl:text>warning</xsl:text>
								</xsl:if>
								<xsl:if test="(@sev>3)or((@sev=3)and(not (@urgent)))">
									<xsl:text>info</xsl:text>
								</xsl:if>
							</xsl:attribute>							
							<xsl:attribute name="message">
								<xsl:value-of select="@msg"/>
							</xsl:attribute>
							<xsl:attribute name="source">
								<xsl:value-of select="@rule"/>
							</xsl:attribute>
						</xsl:element>
					
    				</xsl:for-each>
				</xsl:element>

			</xsl:for-each-group>

		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
