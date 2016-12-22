package ru.agentlab.mystemwrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.osgi.framework.Bundle;

import com.google.common.base.Joiner;

/**
 * Created by Evgeny on 22.12.2014.
 */
public class StemmerWrapper
{
    private static class StemmerWrapperHolder
    {
        public static final StemmerWrapper instance = new StemmerWrapper();
    }

    public static StemmerWrapper getInstance()
    {
        return StemmerWrapperHolder.instance;
    }

    private Properties prop = new Properties();
    private JSONParser parser = new JSONParser();

    private StemmerWrapper()
    {

        URL url = null;

        try
        {
            url = new URL("platform:/plugin/ru.agentlab.mystemwrapper.mac/mystem.properties"); //$NON-NLS-1$
        }
        catch (MalformedURLException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
//        InputStream in =
//            getClass().getResourceAsStream("platform:/plugin/ru.agentlab.mystemwrapper.feature/mystem.properties"); //$NON-NLS-1$

        InputStream in = null;
        try
        {
            in = url.openConnection().getInputStream();
        }
        catch (IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try
        {
            prop.load(in);
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
	 * @param words -- must be UTF-8 encoded
	 * @return
	 * @throws ParseException
	 */
    public List<Word> analysis(Collection<String> words) throws ParseException
    {
        List<Word> result = new ArrayList<Word>();
        List<String> parsed;
        try
        {
            parsed = mystemPartOfWork(words);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return result;
        }


        for (String wordInJson : parsed)
        {
            JSONObject root = (JSONObject) parser.parse(wordInJson);
            Word word = new Word();
            for (Object objKey : root.keySet())
            {
                String key = (String) objKey;
                if ("text".equals(key))
                {
                    String textWord = (String) root.get(key);
                    word.setValue(textWord);
                    continue;
                }

                if ("analysis".equals(key))
                {
                    JSONArray jsonArray = (JSONArray) root.get(key);
                    for (Object object : jsonArray)
                    {
                        Word.Lexema lexema = new Word.Lexema();
                        JSONObject lex = (JSONObject) object;
                        for (Object lexObjKey : lex.keySet())
                        {
                            String lexKey = (String) lexObjKey;
                            if ("lex".equals(lexKey))
                            {
                                String textLex = (String) lex.get(lexKey);
                                lexema.setValue(textLex);
                                continue;
                            }

                            if ("gr".equals(lexKey))
                            {
                                String grLex = (String) lex.get(lexKey);
                                lexema.setGrammemes(convertToGramemes(grLex));
                                continue;
                            }

                            if ("wt".equals(lexKey))
                            {
                                String wtLex = "" + lex.get(lexKey);
                                lexema.setWeight(Double.parseDouble(wtLex));
                                continue;
                            }
                        }
                        word.getLexemas().add(lexema);
                    }
                }
            }
            result.add(word);
        }
        return result;
    }

    private Set<GrammemeType> convertToGramemes(String grLex)
    {
        Set<GrammemeType> result = new HashSet<GrammemeType>();
        if (null == grLex) {
			return result;
		}

        String[] parts = grLex.split("[,=]");
        for (String part : parts)
        {
            if (part.startsWith("1") || part.startsWith("2") || part.startsWith("3"))
            {
                part = "_" + part;
            }

            result.add(GrammemeType.valueOf(part));
        }

        return result;
    }

    public static String getPluginDir(String pluginId) {
        /* get bundle with the specified id */
        // pluginId = pluginId.
        Bundle bundle = Platform.getBundle(pluginId);
        if (bundle == null)
        {
            throw new RuntimeException("Could not resolve plugin: " + pluginId + "\r\n"
                + "Probably the plugin has not been correctly installed.\r\n"
                + "Running eclipse from shell with -clean option may rectify installation.");
        }

        /* resolve Bundle::getEntry to local URL */
        URL pluginURL = null;
        try
        {
            pluginURL = Platform.resolve(bundle.getEntry("/"));
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not get installation directory of the plugin: " + pluginId);
        }
        String pluginInstallDir = pluginURL.getPath().trim();
        if (pluginInstallDir.length() == 0)
        {
            throw new RuntimeException("Could not get installation directory of the plugin: " + pluginId);
        }

        /* since path returned by URL::getPath starts with a forward slash, that
         * is not suitable to run commandlines on Windows-OS, but for Unix-based
         * OSes it is needed. So strip one character for windows. There seems
         * to be no other clean way of doing this. */
        /* if (Platform.getOS().compareTo(Platform.OS_MACOSX) == 0)
        {
            pluginInstallDir = pluginInstallDir.substring(1);
        }*/

        return pluginInstallDir;
    }

    public List<String> mystemPartOfWork(Collection<String> words) throws IOException
    {
        String pathToMystem = prop.getProperty("path_to_mystem_mac");

        String absPath = getPluginDir(pathToMystem);

        /* URL url = new URL(pathToMystem);
        URI pathToMystem_uri = null;
        try
        {
            pathToMystem_uri = url.toURI();
        }
        catch (URISyntaxException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        File f = new File(pathToMystem);*/
        absPath = absPath + "mystem";
        String[] commands =
            new String[] { absPath, "-nid", "--weight", "--eng-gr", "--format", "json", "--generate-all" };
        Process proc = Runtime.getRuntime().exec(commands);
        String input = Joiner.on(' ').skipNulls().join(words);

        PrintWriter pw = new PrintWriter(proc.getOutputStream());
        pw.write(input);
        pw.close();

        List<String> result = new ArrayList<String>();
        InputStream in = proc.getInputStream();
        Scanner sc = new Scanner(in);
        while (sc.hasNextLine()) {
			result.add(sc.nextLine().trim());
		}
        sc.close();

        return result;
    }
}
