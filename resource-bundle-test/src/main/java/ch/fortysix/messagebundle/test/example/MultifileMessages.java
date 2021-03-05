package ch.fortysix.messagebundle.test.example;

import ch.fortysix.resourcebundle.annotation.ResourceBundle;

@ResourceBundle(bundle = "multifile", filesToAnalyse = { "multifile_de.properties", "multifile_en.properties",
		"multifile.properties" })
public class MultifileMessages {

}