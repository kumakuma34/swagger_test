package snsservice.snsservice.TokenTest;

import TemplateClass.BaseTimeEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import snsservice.snsservice.DTO.FeedDTO;
import snsservice.snsservice.Entity.Feed;
import snsservice.snsservice.service.FeedService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FeedTest {
    @Autowired
    FeedService feedService;


    @Test
    public void igFeedTest(){
        //Given
        String ig_Token = "IGQVJYYXFBaXdQUmtrd0NqTzhOVWJrSmQ4QXdNdzZAMVklGSlQ2cHQ4cjdrXzFFNGpSeEtNMlpWQUNPTnFFRGJILVdUODNtVkw3aHVjeE1tUTdpb2JqNngtUFVuM1VsMmMwaTBha3BIV2dmWU8zSmk4ZAmtUU19xTnVmcU5j";
        String time = "2022-05-07-23-30";
        //When
        ArrayList<FeedDTO.PostDTO> list = feedService.getIgFeed(ig_Token, time, false);

        //Then
        for(int i = 0 ; i < list.size(); i++){
            System.out.println(list.get(i).toString());
        }
        feedService.addFeed(list,"17841453112343973","instagram");
    }
    @Test
    public void dateTest(){
        //Given
        String dateStr = "2022-05-07-13-18";
        String postTime = "2022-05-07T04:20:00+0000";

        //when
        SimpleDateFormat formatter = new SimpleDateFormat(("yyyy-mm-dd-hh-mm"));

        try{
            Date date = formatter.parse(dateStr);
            System.out.println(date);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        boolean flag = BaseTimeEntity.compareTime(postTime, dateStr, false);

        //then
        System.out.println(flag);
        assertEquals(true, flag);
    }

    @Test
    public void getAllFeed(){
        //Given
        String user_id = "110097898362018";

        //when
        List<FeedDTO.responseDTO> feedList = feedService.getAllFeed(user_id, "d123");

        /*//Then
        for(int i = 0; i< feedList.size(); i++){
            System.out.println(feedList.get(i).toString());
        }*/
    }

    @Test
    public void dateTest2(){
        String str = "2022-05-07";
        String res = BaseTimeEntity.dayToString(str);
        System.out.println(res);

        //String str = BaseTimeEntity.Dateformatting()
        //System.out.println(str);
    }

    @Test
    public void feedSearchTest(){
        String keyword = "밤";
        String fb_id = "110097898362018";
        String ig_id = "17841453112343973";
        ArrayList<FeedDTO.responseDTO> list = new ArrayList<>();
        list = feedService.searchFeedByKeyword(fb_id, ig_id, keyword);

        System.out.println(list);
    }
}
