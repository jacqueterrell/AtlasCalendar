package com.team.mamba.atlascalendar.userInterface.dashBoard;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.*;
import org.mockito.*;


@SuppressWarnings("ResultOfMethodCallIgnored")
public class InfoDataModelTest {

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void checkAllConnections() {

        List<Integer> connectionString = new ArrayList<>();
        Map<Integer,Integer> connectionsMap = new LinkedHashMap<>();

        connectionString.add(7);
        connectionString.add(7);
        connectionString.add(6);


        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);

        for (int i = 0; i < 6; i++){

            calendar.set(Calendar.MONTH, currentMonth - i);

            connectionsMap.put(currentMonth,0); //7,6,5,4,3,2
            currentMonth -=1;

        }

            for (Integer months: connectionString){

                for (Map.Entry<Integer,Integer> entry : connectionsMap.entrySet()){

                    int monthKey = entry.getKey();
                    int count = entry.getValue();

                    if (monthKey == months){

                        entry.setValue(count + 1);
                    }
                }

        }

        assertEquals(6,connectionsMap.size());
        assertTrue(connectionsMap.get(7).equals(2));
        assertTrue(connectionsMap.get(6).equals(1));
        assertTrue(connectionsMap.get(5).equals(0));
        assertTrue(connectionsMap.get(4).equals(0));
        assertTrue(connectionsMap.get(3).equals(0));
        assertTrue(connectionsMap.get(2).equals(0));


    }

    @Test
    public void testLongList(){

        List<Long> timeStampList = new ArrayList<>();
        timeStampList.add(1531600332L);
        timeStampList.add(1531847343L);
        timeStampList.add(1532962371L);
        timeStampList.add(1533441076L);
        timeStampList.add(1533442364L);

        assertTrue(timeStampList.get(0) == 1531600332);

    }

    @Test
    public void sortLongList(){

        List<String> timeStampList = new ArrayList<>();
        timeStampList.add("1531600332"); //sofr july 14
        timeStampList.add("1532962370"); //Mathew Maher july 30
        timeStampList.add("1533442364"); //Matthew Maher Aug 4
        timeStampList.add("1533441076"); //Jacque Terrell Aug 4
        timeStampList.add("1531847343"); //ramos july 17


        String secondString = timeStampList.get(1);

        //return o1.getName().compareTo(o2.getName());

        Collections.sort(timeStampList,(o1,o2) ->{

//            BigInteger first = new BigInteger(o1);
//            BigInteger second = new BigInteger(o2);

            long first = Long.parseLong(o1);
            long second = Long.parseLong(o2);

            return Long.compare(second, first);

          //  return second.compareTo(first);
        });

        assertFalse(timeStampList.get(1) == secondString);

    }
}