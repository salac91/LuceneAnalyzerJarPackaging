import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

public class NonStemmerSerbianAnalyzer extends Analyzer {

	Version version;
	
	private static final String[] SERBIAN_STOP_WORDS = {
		"biti", "ne", 
		"jesam", "sam", "jesi", "si", "je", "jesmo", "smo", "jeste", "ste", "jesu", "su",
		"nijesam", "nisam", "nijesi", "nisi", "nije", "nijesmo", "nismo", "nijeste", "niste", "nijesu", "nisu",
		"budem", "budeš", "bude", "budemo", "budete", "budu",
		"budes",
		"bih",  "bi", "bismo", "biste", "biše", 
		"bise",
		"bio", "bili", "budimo", "budite", "bila", "bilo", "bile", 
		"ću", "ćeš", "će", "ćemo", "ćete", 
		"neću", "nećeš", "neće", "nećemo", "nećete", 
		"cu", "ces", "ce", "cemo", "cete",
		"necu", "neces", "nece", "necemo", "necete",
		"mogu", "možeš", "može", "možemo", "možete",
		"mozes", "moze", "mozemo", "mozete",
		"li", "da"};

	
	public NonStemmerSerbianAnalyzer(Version version) {
		super();
		this.version = version;
	}
	
	public NonStemmerSerbianAnalyzer(){
		super();
		this.version = Version.LUCENE_4_9;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		Tokenizer source = new StandardTokenizer(version,reader);
		
		//filter za mala slova
		TokenStream result = new LCFilter(source);
		//cirilica u latinicu filter
		result = new CyrilicToLatinFilter(result);
		//filter stop reci
		result = new StopFilter(version, result, StopFilter.makeStopSet(version, SERBIAN_STOP_WORDS));
		//filter koji ce da ukloni akcente - ascii folding
		result = new ASCIIFoldingFilter(result);
		
		return new TokenStreamComponents(source, result);
	}

}
