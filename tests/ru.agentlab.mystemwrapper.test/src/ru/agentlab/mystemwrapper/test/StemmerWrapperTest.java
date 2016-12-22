package ru.agentlab.mystemwrapper.test;

//import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.simple.parser.ParseException;
import org.junit.Test;

import junit.framework.Assert;
import ru.agentlab.mystemwrapper.GrammemeType;
import ru.agentlab.mystemwrapper.StemmerWrapper;
import ru.agentlab.mystemwrapper.Word;

/**
 * Created by Evgeny on 24.12.2014.
 */
public class StemmerWrapperTest
{
    @Test
    public void analysisTest() throws ParseException
    {
    	String s = "программы";
		List<String> line = new ArrayList<>();
        line.add(s);

        List<Word> words = StemmerWrapper.getInstance().analysis(line);
        Word word = words.get(0);

        Assert.assertEquals("программы", word.getValue());

        Set<Word.Lexema> lexemas = word.getLexemas();
        Word.Lexema lexema = lexemas.iterator().next();

        Assert.assertEquals("программа", lexema.getValue());
        Assert.assertEquals(1.0, lexema.getWeight());
        Assert.assertEquals(true, lexema.getGrammemes().contains(GrammemeType.S));
    }
}
