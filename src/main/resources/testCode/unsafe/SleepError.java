public class Main {
    public static void main(String[] args) {
        try {
            long l = 60L * 60L * 1000L; // 1 hour in milliseconds
            Thread.sleep(l);
            System.out.println("程序执行完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}