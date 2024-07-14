package pl.kurs.task1.app;

import pl.kurs.task1.model.StringContainer;

import java.io.IOException;
import java.time.LocalDateTime;

public class StringContainerRunner {
    public static void main(String[] args) throws IOException {
        StringContainer st = new StringContainer("\\d{2}[-]\\d{3}");

        st.add("02-495");//git
        st.add("01-120");//git
        st.add("05-123");//git
        st.add("00-000");//git

        //st.add("ala ma kota"); //powinno sie wywalic wyjatkiem InvalidStringContainerValueException(badValue)
        for (int i = 0; i < st.size(); i++) {
            System.out.println(st.get(i));
        }

        st.remove(0);  //usuwa "02-495"
        st.remove("00-000"); // usuwa "00-000"

        System.out.println("po usunieciu:");
        for (int i = 0; i < st.size(); i++) {
            System.out.println(st.get(i));
        }

        StringContainer st2 = new StringContainer("\\d{2}[-]\\d{3}", true); //jakis parametr np: duplicatedNotAllowed - domyslnie false wtedy np:
        st2.add("02-495");//git
        //st2.add("02-495");//powinno rzucic wyjatkiem DuplicatedElementOnListException

        StringContainer stBetween = st.getDataBetween(null, LocalDateTime.of(2024, 7, 5, 20, 00));
        //ktora zwroci elementy dodane pomiedzy dateFrom a dateTo
        //gdzie dateFrom i dateTo to obiekty LocalDateTime i moga byc nullami.
        //dodatkowo pomysl o persystencji StringContainer tj:
        st.storeToFile("postalCodes.txt"); // powinno zapisac zawartosc
        StringContainer fromFile = StringContainer.fromFile("postalCodes.txt"); // powinno wczytac zawartosc z pliku i "fromFile" musi miec te same dane co "st"

        System.out.println("Loaded from file:");
        for (int i = 0; i < fromFile.size(); i++) {
            System.out.println(fromFile.get(i));
        }

//        fromFile.add("ala ma kota");

        fromFile.add("00-117");
        fromFile.add("99-100");

        System.out.println("Loaded from file:");
        for (int i = 0; i < fromFile.size(); i++) {
            System.out.println(fromFile.get(i));
        }

    }
}
