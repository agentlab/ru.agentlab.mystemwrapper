package ru.agentlab.mystemwrapper;

import org.json.simple.parser.ParseException;

import java.util.*;

/**
 * Created by Evgeny on 25.12.2014.
 */
public class StemmerService
{
    private static class StemmerServiceHolder
    {
        public static final StemmerService instance = new StemmerService();
    }

    public static StemmerService getInstance()
    {
        return StemmerServiceHolder.instance;
    }

    private StemmerService()
    {
    }

    private StemmerWrapper stemmerWrapper = StemmerWrapper.getInstance();
    private Map<String, Word> cash = new TreeMap<String, Word>();

    public Word analysisWord(String s)
    {
        if (null == s)
            return null;

        s = s.toLowerCase();
        if (cash.containsKey(s))
            return cash.get(s);


        List<String> strings = new ArrayList<String>();
        strings.add(s);
        try
        {
            List<Word> words = stemmerWrapper.analysis(strings);
            Word word = words.get(0);
            cash.put(word.getValue(), word);
            return word;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public Set<Word> analysisWords(Collection<String> strings)
    {
        Set<Word> result = new HashSet<Word>();
        Set<String> buffer = new TreeSet<String>();
        for (String s : strings)
        {
            if (null == s)
                continue;

            s = s.toLowerCase();
            if (cash.containsKey(s))
            {
                result.add(cash.get(s));
                continue;
            }

            buffer.add(s);
        }

        try
        {
            List<Word> words = stemmerWrapper.analysis(buffer);
            for (Word word : words)
            {
                cash.put(word.getValue(), word);
            }
            result.addAll(words);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return result;
    }
}
