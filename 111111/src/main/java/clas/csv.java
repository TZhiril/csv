package clas;
import java.io.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.List;

public class csv {

    //здесь хранятся данные таблицы
    public List table = new ArrayList();
    String nameFile = "";

    //Декоратор
    public csv(String nameFile) throws UnsupportedEncodingException {
        this.nameFile = nameFile;

        //Проверка существования файла
        File f = new File(nameFile);
        if(!(f.exists() && !f.isDirectory())) {
            //Файл не существует, создадим пустой файл с оглавлением(с названиями столбцов)
            this.table.clear();
            String default_stb = "id;amount;mcc_code;tr_type";
            this.table.add(default_stb.split(";"));

            //Создаст новый файл с дефолтный значением оглавления столбцов
            this.read_file_with_table();
        }
        //записываем данные из файла в таблицу(List)
        this.read_table_with_file();
        this.print_table();
    }

    //Поменять в листе элементы i и j
    //Вернет true в случае успеха
    public static <E> boolean swap_elem_list(List<E> list, int i, int j) {
        if (i < 1 || j < 1 || i >=  list.size() || j >=  list.size()) return false;
        E e = list.get(i);
        list.set(i, list.get(j));
        list.set(j, e);
        return true; //Успешный обмен
    }

    //Расширение массива(так же и сужение, но мы не используем это)
    private static String[] extendArraySize(String [] array, int newLen){
        String [] temp = array.clone();
        array = new String[newLen];
        System.arraycopy(temp, 0, array, 0, temp.length);
        int i = temp.length;
        while (i < newLen){
            array[i] = "";
            i++;
        }
        return array;
    }

    //Записать данные из файла в таблицу
    public void read_table_with_file() {
        //Очищаем таблицу
        this.table.clear();

        try(FileReader reader = new FileReader(this.nameFile))
        {
            String StrData = "";
            int ch;

            //Считываем данные в строку
            ch = reader.read();
            while(ch != -1){ // -1 - символ конца файла
                StrData += (char)ch;
                ch = reader.read();
            }
            //закрывает файл
            reader.close();
            // Преобразовываем строку в массив строк, в каждой строке определенная строка таблицы в виде строк
            String[] parts = StrData.replace("\r", "").split("\n");


            // Преобразовываем массив строк в List, в каждой строке массив строк, каждая строка соответсвует значению в ячейки в таблице
            for (int i = 0; i < parts.length; i++) {
                this.table.add(parts[i].split(";"));
                // решение проблемы по типу 6;100;; , где окончание пустое, не верно работат split в таких случаях
                if (((String[])this.table.get(i)).length != ((String[])this.table.get(0)).length){
                    this.table.set(i, this.extendArraySize((String[])this.table.get(i), ((String[])this.table.get(0)).length));
                }
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    //Перезапись файла
    public void read_file_with_table() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(this.nameFile));
            int len_str =  ((String[])this.table.get(0)).length;
            for (int i = 0; i < this.table.size(); i++) {
                for (int j = 0; j < len_str; j++) {
                    try {
                        pw.printf(((String[])this.table.get(i))[j] + ";");
                    }
                    catch (Exception e) {
                        pw.printf(";");
                    }
                }
                pw.printf("\n");
            }
            pw.close();
            System.out.println("finished");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //Расставляет правильно индексы в столбце id
    public void refresh_stb_id(){
        for (int i = 1; i < this.table.size(); i++) {
            ((String[])this.table.get(i))[0] = Integer.toString(i);
        }
    }

    //Удаляет строку под номером  id
    public String del_str(int id){
        try {
            int lenTable = this.table.size();
            if (lenTable <= id || id < 1) {
                return "Данной строки не существует, удаление не возможно!\n";
            } else {
                this.table.remove(id);
                this.refresh_stb_id();
                return "OK!";
            }
        }catch (Exception e){
            return "Неизвестная ошибка!";
        }
    }

    //Вставит строку ниже указаной в id, вставить в начал id = 0
    //Возвращает ответ на запрос
    //dataStr - данные строки таблицы в виде строки, пример "1;245;653"
    public String add_str(int id, String strData){
        try {
            int lenTable = this.table.size();
            if (lenTable < id || id < 0) {
                return "НЕ ВОЗМОЖНО Вставить строку ниже указанной!\n";
            } else {
                //Добавим в строку данныхпорядковы id(пока )
                //переводим строку в массив строк, пример "1;245;653" -> {"1","245","653}
                String[] add_date = strData.split(";");
                //Нам нужно проверить, что колличество столбцов в вводимой строке совпадает и колличеством стб в таблице
                int len_str_table = ((String[]) this.table.get(0)).length;
                if (len_str_table != add_date.length){
                    return "Некорректный формат вводимых данных\n";
                }
                //Добавляем в случае успеха
                this.table.add(id + 1, add_date);
                this.refresh_stb_id();
                return "OK!\n";
            }
        }catch (Exception e){
            return "Неизвестная ошибка\n\"";
        }
    }

    //Выводит значение таблицы на экран
    public void print_table(){
        int len_str =  ((String[])this.table.get(0)).length;
        for (int i = 0; i < this.table.size(); i++) {
            for (int j = 0; j < len_str; j++) {
                try {
                    System.out.format("|%10s", ((String[])this.table.get(i))[j]);
                }
                catch (Exception e) {
                    System.out.format("|%10s", "");
                }
            }
            System.out.format("|\n");
        }
    }

    //Обычный поиск
    private String median_standart(List table){
        int len_table = table.size();
        if (len_table % 2 == 0){
            try {
                //Преобразовываем строку в число
                double res = Double.parseDouble(((String[])table.get(len_table / 2))[1].replace(',', '.'));
                //округление
                res = ((double)((int)(res * 10000 + 0.5)))/10000;
                return Double.toString(res);
            }catch (Exception e) {
                //Преобразовывать строку в число не вышло, значит вывод ошибки
                return "В медианной ячейке введен не верный формат числа";
            }
        }else{
            try {
                //Преобразовываем строки в число
                double med1 = Double.parseDouble(((String[])table.get((len_table - 1) / 2))[1].replace(',', '.'));
                double med2 = Double.parseDouble(((String[])table.get((len_table + 1) / 2))[1].replace(',', '.'));
                double res = (med1 + med2)/ 2;
                //округление
                res = ((double) ((int)(res * 10000 + 0.5)))/10000;
                return Double.toString(res);
            }catch (Exception e) {
                //Преобразовывать строку в число не вышло, значит вывод ошибки
                return "В какой-то медианной ячейке введен не верный формат числа";
            }
        }

    }

    //Пользовательская функция вычисления медианы с выбором опций
    public String median(int param){
        if (this.table.size() < 2){
            return "Таблица пуста!";
        }
        if(param == 0){
            return this.median_standart(this.table);
        }else if  (param == 1){
            List table_reserv = new ArrayList();

            int len_str =  ((String[])this.table.get(0)).length;
            //Отображает нормальная ли строки или нет(плохая строка имеет пустые строки)
            boolean flag;
            for (int i = 0; i < this.table.size(); i++) {
                //Предпологаем, что данная строка хорошая(не имеет пустых строк)
                flag = true;
                for (int j = 0; j < len_str; j++){
                    if (((String[])this.table.get(i))[j] == ""){
                        flag = false;
                        break;
                    }
                }
                //Если строка не содержит пустые строки, то добавляем ее в новый лист
                if (flag) table_reserv.add(this.table.get(i));
            }
            if (table_reserv.size() > 1) return this.median_standart(table_reserv);
            else return "Все строки имеют в наличии пустые ячейки!";
        }else if  (param == 2){
            //Копируем таблицу
            List table_reserv = new ArrayList();
            table_reserv.addAll(this.table);

            //Сортировка по полю amount (Сортировка пузырьком)
            double num1, num2;
            for (int i = 0; i < table_reserv.size() - 1; i++) {
                for (int j = 2; j < table_reserv.size() - i ; j++) {
                    try{
                        num1 =  Double.parseDouble(((String[])table_reserv.get(j - 1))[1].replace(',', '.'));
                    }catch (Exception e){
                        //В случпе пустых строк или не корректрых строк, будем считать что значение имеет наименьшее значение
                        num1 =  - Double.POSITIVE_INFINITY;
                    }
                    try{
                        num2=  Double.parseDouble(((String[])table_reserv.get(j))[1].replace(',', '.'));
                    }catch (Exception e){
                        //В случае пустых строк или не корректрых строк, будем считать что значение имеет наименьшее значение
                        num2 =  - Double.POSITIVE_INFINITY;
                    }
                    //Перестановка элементов если левый(верхний) больше правого(нижний)
                    if (num1 > num2)this.swap_elem_list(table_reserv, j - 1, j);
                    //if ()swap_elem_list
                }
            }

            //Удаляем дубли
            int i = table_reserv.size() - 1;
            int j;
            String key = "";
            String key2 = "";
            while (i > 1){
                key = (String)(((String [])table_reserv.get(i))[2] + " + " + ((String [])table_reserv.get(i))[3]);
                j = i - 1;
                while (j > 0){
                    key2 =  (String)(((String [])table_reserv.get(j))[2] + " + " + ((String [])table_reserv.get(j))[3]);

                    if(new String(key).equals(key2)){
                        table_reserv.remove(j);
                        i--;
                    }
                    j--;
                }
                i--;
            }
            if (table_reserv.size() > 1) return this.median_standart(table_reserv);
            else return "Error!";
        }
        return "Error!";
    }
}