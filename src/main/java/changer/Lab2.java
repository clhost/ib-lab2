package changer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

@SuppressWarnings({"Duplicates", "ResultOfMethodCallIgnored"})
public class Lab2 {
    private static final Scanner sc = new Scanner(System.in);
    private static final String MASK = "dd/MM/yyyy hh:mm:ss";

    public static void main(String[] args) throws IOException, ParseException {
        String[] cmd = new String[]{"/bin/bash", createTempScript().getAbsolutePath()};
        Process pr = Runtime.getRuntime().exec(cmd);

        BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));

        String line = reader.readLine();
        String code;
        try {
            code = new String(Files.readAllBytes(Paths.get(".s")), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("License file wasn't found. Abort.");
            return;
        }

        line = line.replace("\n", "").replace("\r", "");
        code = code.replace("\n", "").replace("\r", "");

        if (line.equals(code)) {
            program();
        } else {
            System.out.println("Abort.");
        }
    }

    private static File createTempScript() throws IOException {
        File tempScript = File.createTempFile("script", null);
        tempScript.setExecutable(true);

        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
                tempScript));
        PrintWriter printWriter = new PrintWriter(streamWriter);
        printWriter.print(
                "#!/bin/bash\n" +
                        "function extract_serial_id () {\n" +
                        "    target_name=$1\n" +
                        "    out=\"$(lsblk | grep '^[a-z]' | cut -f 1 -d \" \")\"\n" +
                        "    IFS=$'\\n' read -rd '' -a disks <<< \"$out\"\n" +
                        "\n" +
                        "    for d in \"${disks[@]}\"\n" +
                        "    do\n" +
                        "        if [[ \"$target_name\" == \"$d\"* ]]; then\n" +
                        "            con=\"$(udevadm info --query=all --name=/dev/$d | grep ID_SERIAL_SHORT | cut -f 2 -d \"=\")\"\n" +
                        "            con=\"$con some_salt\"\n" +
                        "            echo \"$(md5sum <<< $con | cut -f 1 -d \" \")\"\n" +
                        "        fi\n" +
                        "    done\n" +
                        "}\n" +
                        "\n" +
                        "\n" +
                        "target_path=" + System.getProperty("user.dir") + "\n" +
                        "a=\"$(lsblk | grep '^[^A-Za-z0-9]')\"\n" +
                        "\n" +
                        "IFS=$'\\n' read -rd '' -a lines <<< \"$a\"\n" +
                        "len=0\n" +
                        "for i in \"${lines[@]}\"\n" +
                        "do\n" +
                        "    IFS=$' ' read -rd '' -a arr <<< \"$(echo $i | cut -d \" \" -f 1,7)\"\n" +
                        "    t=${arr[1]}\n" +
                        "    if [[ \"$target_path\" == $(echo $t)* ]]; then\n" +
                        "        if [[ ${#t} > $len ]]; then\n" +
                        "            len=${#t}\n" +
                        "            first=${arr[0]}\n" +
                        "            second=$t\n" +
                        "        fi\n" +
                        "    fi\n" +
                        "done\n" +
                        "\n" +
                        "e=\"$(echo ${first} | grep -Po [A-Za-z0-9]+)\"\n" +
                        "extract_serial_id \"$e\"\n"
        );

        printWriter.close();
        return tempScript;
    }

    private static void program() throws ParseException, FileNotFoundException {
        // read path
        System.out.println("Specify path to file: ");
        String path = sc.nextLine();
        System.out.println("\n");

        // check file
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("File " + path + " doesn't exists.");
        }

        // read date
        System.out.println("Specify date by " + MASK + " format: ");
        String d = sc.nextLine();
        System.out.println("\n");

        // parse date
        SimpleDateFormat format = new SimpleDateFormat(MASK);
        Date date = format.parse(d);

        // print info
        System.out.println("Original last modified: " + format.format(file.lastModified()));
        file.setLastModified(date.getTime());
        System.out.println("New last modified: " + format.format(file.lastModified()));
    }
}
