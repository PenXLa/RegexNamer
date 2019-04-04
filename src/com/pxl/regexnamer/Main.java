package com.pxl.regexnamer;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

public class Main {

    public static void main(String[] args) {
	    if (args.length!=0) {

        } else {
            shellMode();
        }
    }

    public static void shellMode() {
        Scanner in = new Scanner(System.in);
        while(true) {
            String line = in.nextLine();
            Pattern pat = Pattern.compile("(.+?)( .*|$)");
            Matcher matcher = pat.matcher(line);
            matcher.find();
            String cmd = matcher.group(1).toLowerCase();
            if (cmd.equals("help")) {
                //System.out.println("del pattern [-f] [-d] [-c] [-p]\tdelete files and folders that match the pattern.\n\tpattern:the regular expression to match files and folders\n\teg. del \"^\\d.txt$\". This example deletes text files that name is a digit.\n");
                System.out.println("rename pattern replacement [-f] [-d] [-c] [-p]\trename files and folders that match the pattern to the replacement.\n\tpattern:the regular expression to match files and folders\n\treplacement:new file/folder name\n\teg. rename \"(\\d)\\.txt\" \"a$1.txt\"\n");
                System.out.println("about optional flags:\n\t-f:process files\n\t-d:process folders\n\t-c:process child directory\n\t-p:preview items that will be processed(just preview, no real files and folders will be processed)\nif no flags is used, the program only process files");
            } else if (cmd.equals("rename") || cmd.equals("del")) {
                boolean ff=false,fd=false,fc=false,fp=false;
                pat = Pattern.compile(".+? \"(.*?)\" \"(.*?)\"(.*$)");
                matcher = pat.matcher(line);
                matcher.find();

                Matcher flagMatcher = Pattern.compile("-(.)").matcher(matcher.group(3));
                if (flagMatcher.matches()) {
                    ff = true;
                } else {
                    while(flagMatcher.find()) {
                        String s= flagMatcher.group(1);
                        ff = ff||s.equals("f");
                        fd = fd||s.equals("d");
                        fc = fc||s.equals("c");
                        fp = fp||s.equals("p");
                    }
                }

                if (cmd.equals("rename"))
                    rename(new File("D:\\a\\"),Pattern.compile(matcher.group(1)),matcher.group(2),ff,fd,fc,fp);
                else
                    System.out.println("coming soon..");
                System.out.println("Finished");
            }

        }
    }


    public static void rename(File dir, Pattern pat, String rep, boolean processFile, boolean processFolder, boolean processChild, boolean previewMode) {
        File[] fs = dir.listFiles();
        for (File f : fs) {
            Matcher matcher = pat.matcher(f.getName());
            if (matcher.matches()) {//如果符合pattern
                File newf = new File((f.getParent()+'\\' +matcher.replaceAll(rep)).replace("\\\\","\\"));
                if (f.isDirectory() && processFolder || f.isFile() && processFile) {
                    if (previewMode) {
                        System.out.println(f.getName() + " --> " +newf.getName());
                    } else {
                        f.renameTo(newf);
                    }
                    if (f.isDirectory() && processChild) {
                        //穿透子文件夹
                        rename(f, pat, rep, processFile, processFolder, processChild, previewMode);
                    }
                }
            }
        }
    }
}
