package com.android.willen.autoshutdown.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Pair;

public class ProcessHelper
{
        /**
         * run external command
         * 
         * @param addCommandline
         *            add commandline to stdout
         * @param commands
         *            list of command and arguments to run
         * @return Pair of exitCode and stdout/stderr
         */
        static public Pair<Integer, String> runCmd(boolean addCommandline, String... commands)
        {
                final int BUFSIZE = 1024;
                StringBuilder sb = new StringBuilder(BUFSIZE);

                if (addCommandline)
                {
                        sb.append("$ ");
                        // add input string to stdout
                        for (String c : commands)
                        {
                                sb.append(c);
                                sb.append(" ");
                        }
                        sb.append("\n");
                }

                int exitVal = -1;
                ProcessBuilder builder = new ProcessBuilder(commands);
                builder.redirectErrorStream(true);
                try
                {
                        Process p = builder.start();

                        InputStream in = p.getInputStream();
                        InputStreamReader isr = new InputStreamReader(in);
                        char[] buf = new char[BUFSIZE];
                        int len = -1;
                        while (-1 != (len = isr.read(buf)))
                        {
                                sb.append(buf, 0, len);
                        }
                        exitVal = p.waitFor();

                        p.destroy();
                } catch (IOException e)
                {
                        e.printStackTrace();
                } catch (InterruptedException e)
                {
                        e.printStackTrace();
                }

                return new Pair<Integer, String>(exitVal, sb.toString());
        }

}