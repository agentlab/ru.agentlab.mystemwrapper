package ru.agentlab.mystemwrapper.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import ru.agentlab.mystemwrapper.GrammemeType;
import ru.agentlab.mystemwrapper.StemmerWrapper;
import ru.agentlab.mystemwrapper.Word;

/**
 * Created by Evgeny on 24.12.2014.
 */
public class StemmerWrapperTest {
	@Test
	public void analysisTest() throws ParseException {
		String s = "программы";
		List<String> line = new ArrayList<>();
		line.add(s);

		List<Word> words = StemmerWrapper.getInstance().analysis(line);
		Word word = words.get(0);

		assertThat(word.getValue(), is("программы"));

		Set<Word.Lexema> lexemas = word.getLexemas();
		Word.Lexema lexema = lexemas.iterator().next();

		assertThat(lexema.getValue(), is("программа"));
		assertThat(lexema.getWeight(), is(1.0));
		assertThat(lexema.getGrammemes().contains(GrammemeType.S), is(true));
	}
}
