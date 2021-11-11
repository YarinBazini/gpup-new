<GPUP-Descriptor xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="G.P.U.P-V1.xsd">
	<GPUP-Configuration>
		<GPUP-Graph-Name>Test</GPUP-Graph-Name>
		<GPUP-Working-Directory>c:\temp</GPUP-Working-Directory>
	</GPUP-Configuration>
	<GPUP-Targets>
		<GPUP-Target name="A">
			<GPUP-User-Data>freeText</GPUP-User-Data>
			<GPUP-Target-Dependencies>
				<GPUG-Dependency type="requiredFor">D</GPUG-Dependency>
			</GPUP-Target-Dependencies>
		</GPUP-Target>
		<GPUP-Target name="B">
			<GPUP-Target-Dependencies>
				<GPUG-Dependency type="requiredFor">D</GPUG-Dependency>
				<GPUG-Dependency type="requiredFor">E</GPUG-Dependency>
			</GPUP-Target-Dependencies>
		</GPUP-Target>
		<GPUP-Target name="C">
			<GPUP-User-Data>freeText</GPUP-User-Data>
		</GPUP-Target>
		<GPUP-Target name="D">
			<GPUP-User-Data>freeText</GPUP-User-Data>
		</GPUP-Target>
		<GPUP-Target name="E">
			<GPUP-User-Data>freeText</GPUP-User-Data>
			<GPUP-Target-Dependencies>
				<GPUG-Dependency type="dependsOn">D</GPUG-Dependency>
				<GPUG-Dependency type="dependsOn">C</GPUG-Dependency>
			</GPUP-Target-Dependencies>
		</GPUP-Target>
	</GPUP-Targets>
</GPUP-Descriptor>
