import clas.csv;
import java.io.*;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) throws IOException {
        PrintStream ps = new PrintStream(System.out, true, "UTF-8");
        csv CSV = new csv("data/ooo.csv");

        InputStream inputStream = System.in;
        Reader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        ps.println("__________________________________________________________\n" +
                "Введите номер команды: \n1 - считать из файла(еще раз)  \n2 - записать в файл \n3 - добавление записи, \n4 - удаление записи" +
                "\n5 - вывод БД в консоль \n6 - поиск медианного значения"+
                "\n7 - поиск медианного значения по строкам не содержащие пустые строки"+
                "\n8 - поиск медианного значения по отсортированному полю amount в порядке возрастания  \n9 - Выход из программы \nВведите команду: ");
        String command = bufferedReader.readLine();
        while (!(new String(command).equals("9"))){
            if (command.equals("1")) {
                CSV.read_table_with_file();
            }else if(command.equals("2")) {
                CSV.read_file_with_table();
            }else if(command.equals("3")) {
                ps.print("Ниже какой строки вставить строку? : ");
                int id = Integer.parseInt(bufferedReader.readLine());
                //данные новой строки
                String input_data = ";";
                ps.print("Введите значение amount : ");
                input_data += bufferedReader.readLine() + ";";
                ps.print("Введите значение mcc_code : ");
                input_data += bufferedReader.readLine()+ ";";
                ps.print("Введите значение tr_type : ");
                input_data += bufferedReader.readLine();
                ps.println("Результат: " + CSV.add_str(id, input_data));
            }else if(command.equals("4")) {
                ps.print("Укажите номер удаляемой строки : ");
                int id = Integer.parseInt(bufferedReader.readLine());
                ps.print("Результат: " + CSV.del_str(id)+ '\n');
            }else if(command.equals("5")) {
                CSV.print_table();
            }else if(command.equals("6")) {
                ps.print("Результат: " + CSV.median(0) + '\n');
            }else if(command.equals("7")) {
                ps.print("Результат: " + CSV.median(1) + '\n');
            }else if(command.equals("8")) {
                ps.print("Результат: " + CSV.median(2) + '\n');
            }else ps.print("Не верная команда!\n");

            ps.println("__________________________________________________________\n" +
                    "Введите номер команды: \n1 - считать из файла(еще раз)  \n2 - записать в файл \n3 - добавление записи, \n4 - удаление записи" +
                    "\n5 - вывод БД в консоль \n6 - поиск медианного значения"+
                    "\n7 - поиск медианного значения по строкам не содержащие пустые строки"+
                    "\n8 - поиск медианного значения по отсортированному полю amount в порядке возрастания  \n9 - Выход из программы \nВведите команду: ");
            command = bufferedReader.readLine();
        }
        CSV.print_table();
        ps.print(CSV.median(2));
    }
}
