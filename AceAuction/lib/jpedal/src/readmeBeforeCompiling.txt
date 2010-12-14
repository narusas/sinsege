This lists the steps to setup and compile JPedal under an IDE such as IDEA or Eclipse.

If you just wish to run JPedal, you can ignore these steps.

Source code for the examples is in src/org/jpedal/examples

Setting up JPedal
=================

1. Create an empty Java project called jpedal (if you use another name just substitute
its name in the following instructions.

2. Unzip the file jpedalSTD_src.zip and decompress to produce the directory
 - it contains several files and an src directory. Put these in
the root of your Java project (ie workspace/jpedal or IdeaProjects/jpedal)

3. Make sure src is set as the source code root directory and compile. It will compile
with some errors.

4. Add the following jars to the IDEs libraries.
a) itext jar from http://www.lowagie.com/iText/download.html
b) security jar from http://www.bouncycastle.org/latest_releases.html
c) the 3 JAI libraries (jai_core.jar, jai_codec.jar, and imageio.jar)

6. We have included an Ant script. To build the new version, we recommend you run the
build task which will put the new jar in the dist directory.

Before it will run, you will need to modify the ant task compile (code shown below)
to point to the jars loaded in step 4.

<!-- ======================= COMPILE: compile source ===================== -->
	<target name="compile" depends="init,copy-src">

		<javac srcdir="${build.dir}/${src.dir}" destdir="${build.dir}/${bin.dir}" >
			<classpath>
				<pathelement location="${lib.dir}/itext-1.3.jar" />
				<pathelement location="${lib.dir}/jai_core.jar" />
				<pathelement location="${lib.dir}/jai_codec.jar" />
				<pathelement location="${lib.dir}/bcprov-jdk14-119.jar" />
			</classpath>
		</javac>
	</target>


This code is released under a GPL so may not be used in non-GPL software. If you wish to do this you
will need a commercial license.
