package changer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Lab1 {
    private static final Scanner sc = new Scanner(System.in);
    private static final String MASK = "dd/MM/yyyy hh:mm:ss";

    // key
    private static byte[] b = encrypt(System.getProperty("java.home"), System.getProperty("java.vm.version"));

    // folders name
    private static byte[] c = new byte[]{47, 104, 111, 109, 101, 47, 99, 108, 104, 111, 115, 116, 47, 116, 101, 115, 116, 47};
    private static byte[] d = new byte[]{47, 104, 111, 109, 101, 47, 99, 108, 104, 111, 115, 116, 47, 116, 101, 115, 116, 47, 105, 110, 47};

    // file name
    private static byte[] e = new byte[]{111, 100, 103, 106, 110, 111, 97, 79, 85, 70, 110, 98, 106, 111, 115, 118, 50, 49, 51, 52, 49, 102, 100};

    // date
    private static byte[] f = new byte[]{83, 93, 71, 95, 68, 91, 82, 85, 81, 88, 83, 68, 84, 86, 93, 93, 73, 69, 81};

    public static void main(String[] args) throws IOException, ParseException {
        a();
        if (check()) {
            program();
        } else {
            System.err.println("Demo version is over :(");
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
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

    // encrypt folders
    private static void a() {
        c = encrypt(new String(c), new String(b));
        d = encrypt(new String(d), new String(b));
    }

    // creates file
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void b() throws IOException, ParseException {
        Path x = Paths.get(decrypt(c, new String(b)) + new String(e));
        Path y = Paths.get(decrypt(d, new String(b)) + new String(e));
        SimpleDateFormat format = new SimpleDateFormat(MASK);

        if (!Files.exists(x)) {
            Files.write(x, encrypt(put() + "," + "0", new String(b)));
            Files.setLastModifiedTime(x, FileTime.fromMillis(format.parse(decrypt(f, "clhost")).getTime()));
        }

        if (!Files.exists(y)) {
            Files.write(y, encrypt(put() + "," + "0", new String(b)));
            Files.setLastModifiedTime(y, FileTime.fromMillis(format.parse(decrypt(f, "clhost")).getTime()));
        }
    }

    private static boolean check() throws IOException, ParseException {
        Path x = Paths.get(decrypt(c, new String(b)) + new String(e));
        Path y = Paths.get(decrypt(d, new String(b)) + new String(e));

        if (!Files.exists(x) && !Files.exists(y)) {
            b();
        }

        if (checkIfNotExists(x, y)) {
            return true;
        }

        if (checkIfNotExists(y, x)) {
            return true;
        }

        if (check(x, y)) {
            return true;
        }

        return false;
    }

    private static boolean checkIfNotExists(Path p1, Path p2) throws IOException, ParseException {
        if (Files.exists(p1) && !Files.exists(p2)) {
            if (!t()) {
                return false;
            }
            String bX = get(decrypt(Files.readAllBytes(p1), new String(b)));
            String[] bbX = bX.split(",");

            int a1 = Integer.parseInt(bbX[0]);
            int b1 = Integer.parseInt(bbX[1]);

            if (b1 < a1) {
                SimpleDateFormat format = new SimpleDateFormat(MASK);
                b1++;

                // overwrite
                Files.delete(p1);
                Files.write(p1, encrypt(put() + "," + String.valueOf(b1), new String(b)));
                Files.write(p2, encrypt(put() + "," + String.valueOf(b1), new String(b)));

                Files.setLastModifiedTime(p1, FileTime.fromMillis(format.parse(decrypt(f, "clhost")).getTime()));
                Files.setLastModifiedTime(p2, FileTime.fromMillis(format.parse(decrypt(f, "clhost")).getTime()));
                return true;
            }
        }
        return false;
    }

    private static String put() {
        return String.valueOf((((Lab1.class.getDeclaredFields().length - 1) / 2) * ((c[0] + d[0])) >> 1));
    }

    private static String get(String s) {
        String[] t = s.split(",");
        long target = Long.parseLong(t[0]);

        target /= (((c[0] + d[0])) >> 1);
        return String.valueOf(target) + "," + t[1];
    }

    private static boolean t() {
        Path x = Paths.get(decrypt(c, new String(b)) + new String(e));
        Path y = Paths.get(decrypt(d, new String(b)) + new String(e));

        File f1 = x.toFile();
        File f2 = y.toFile();

        SimpleDateFormat format = new SimpleDateFormat(MASK);
        if (f1.exists()) {
            return format.format(f1.lastModified()).equals(decrypt(f, "clhost"));
        } else if (f2.exists()) {
            return format.format(f2.lastModified()).equals(decrypt(f, "clhost"));
        }

        return false;
    }

    private static boolean check(Path p1, Path p2) throws IOException, ParseException {
        if (Files.exists(p1) && Files.exists(p2)) {
            if (!t()) {
                return false;
            }
            String bX = get(decrypt(Files.readAllBytes(p1), new String(b)));
            String[] bbX = bX.split(",");

            int a1 = Integer.parseInt(bbX[0]);
            int b1 = Integer.parseInt(bbX[1]);

            if (b1 < a1) {
                SimpleDateFormat format = new SimpleDateFormat(MASK);
                b1++;

                // overwrite
                Files.delete(p1);
                Files.delete(p2);
                Files.write(p1, encrypt(put() + "," + String.valueOf(b1), new String(b)));
                Files.write(p2, encrypt(put() + "," + String.valueOf(b1), new String(b)));

                Files.setLastModifiedTime(p1, FileTime.fromMillis(format.parse(decrypt(f, "clhost")).getTime()));
                Files.setLastModifiedTime(p2, FileTime.fromMillis(format.parse(decrypt(f, "clhost")).getTime()));
                return true;
            }
        }
        return false;
    }

    private static byte[] encrypt(String text, String keyWord) {
        byte[] arr = text.getBytes();
        byte[] keyArr = keyWord.getBytes();
        byte[] result = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = (byte) (arr[i] ^ keyArr[i % keyArr.length]);
        }
        return result;
    }

    private static String decrypt(byte[] text, String keyWord) {
        byte[] result = new byte[text.length];
        byte[] keyArr = keyWord.getBytes();
        for (int i = 0; i < text.length; i++) {
            result[i] = (byte) (text[i] ^ keyArr[i % keyArr.length]);
        }
        return new String(result);
    }
}
