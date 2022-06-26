package snsservice.snsservice.controller;

import TemplateClass.BaseTimeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import snsservice.snsservice.DTO.FeedDTO;
import snsservice.snsservice.DTO.TokenDTO;
import snsservice.snsservice.service.FeedService;
import snsservice.snsservice.service.UserService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FeedController {
    @Autowired
    private UserService userservice;
    @Autowired
    private FeedService feedService;
    @CrossOrigin("*")
    @RequestMapping(value = "/update/{time}", method = RequestMethod.GET)
    public Map<String, Object> updateFeed(@PathVariable String time, @RequestHeader(name="Authorization") String accessToken){
        Map<String, Object> response = new LinkedHashMap<>();
        TokenDTO.AccessTokenDTO token = userservice.getAccessToken(accessToken);
        try{
            //facebook
            ArrayList<String> feedList = feedService.getPostListFromFeed(token.getFb_id(),token.getFb_access_token(),time, false);
            ArrayList<FeedDTO.PostDTO> postList = feedService.getPostInfo(feedList,token.getFb_access_token());
            feedService.addFeed(postList , token.getFb_id(),"facebook");

            //instagram
            ArrayList<FeedDTO.PostDTO> ig_postList = feedService.getIgFeed(token.getIg_access_token(),time,false);
            feedService.addFeed(ig_postList,token.getIg_id(),"instagram");

            ArrayList<FeedDTO.responseDTO> res = feedService.getAllFeed(token.getFb_id(), token.getIg_id());
            response.put("feed_list", res);
        }catch(Exception e){
            e.printStackTrace();
        }
        //response.put("fb_id", token.getFb_id());
        //response.put("fb_token", token.getFb_access_token());

        return response;
    }

    @CrossOrigin("*")
    @RequestMapping(value = "/updateNow", method = RequestMethod.GET)
    public Map<String, Object> updateFeedNow(@RequestHeader(name="Authorization") String accessToken){
        Map<String, Object> response = new LinkedHashMap<>();
        TokenDTO.AccessTokenDTO token = userservice.getAccessToken(accessToken);

        String time = "1990-00-00-00-00";

        try{
            //facebook
            ArrayList<String> feedList = feedService.getPostListFromFeed(token.getFb_id(),token.getFb_access_token(),time, false);
            ArrayList<FeedDTO.PostDTO> postList = feedService.getPostInfo(feedList,token.getFb_access_token());
            feedService.addFeed(postList , token.getFb_id(),"facebook");

            //instagram
            ArrayList<FeedDTO.PostDTO> ig_postList = feedService.getIgFeed(token.getIg_access_token(),time, false);
            feedService.addFeed(ig_postList,token.getIg_id(),"instagram");

            ArrayList<FeedDTO.responseDTO> res = feedService.getAllFeed(token.getFb_id(), token.getIg_id());
            response.put("feed_list", res);
        }catch(Exception e){
            e.printStackTrace();
        }
        //response.put("fb_id", token.getFb_id());
        //response.put("fb_token", token.getFb_access_token());

        return response;
    }

    @CrossOrigin("*")
    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    public Map<String, Object> updateFeed(@RequestHeader(name="Authorization") String accessToken){
        Map<String, Object> response = new LinkedHashMap<>();
        TokenDTO.AccessTokenDTO token = userservice.getAccessToken(accessToken);
        ArrayList<FeedDTO.responseDTO> res = feedService.getAllFeed(token.getFb_id(), token.getIg_id());
        response.put("feed_list", res);

        return response;
    }

    @CrossOrigin("*")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Map<String, Object> searchFeed(@RequestParam Map<String, String> param , @RequestHeader(name="Authorization") String accessToken){
        Map<String, Object> response = new LinkedHashMap<>();
        TokenDTO.AccessTokenDTO token = userservice.getAccessToken(accessToken);

        String startDate = param.get("start");
        String endDate = param.get("end");

        ArrayList<FeedDTO.responseDTO> res = feedService.searchFeed(token.getFb_id(), token.getIg_id(),BaseTimeEntity.dayToString(startDate).toString(), BaseTimeEntity.dayToStringAddDay(endDate).toString());
        response.put("feed_list", res);

        return response;
    }

    @CrossOrigin("*")
    @RequestMapping(value = "/forDay", method = RequestMethod.GET)
    public Map<String, Object> searchFeedForDay(@RequestHeader(name="Authorization") String accessToken){
        Map<String, Object> response = new LinkedHashMap<>();
        TokenDTO.AccessTokenDTO token = userservice.getAccessToken(accessToken);

        String startDate = BaseTimeEntity.getDayBeforeString();
        String endDate = BaseTimeEntity.getCurrentTimeString();
        System.out.println(startDate + " ~ " + endDate);

        ArrayList<FeedDTO.responseDTO> res = feedService.searchFeed(token.getFb_id(), token.getIg_id(),BaseTimeEntity.timeToString(startDate).toString(), BaseTimeEntity.timeToString(endDate).toString());
        response.put("feed_list", res);

        return response;
    }

    @CrossOrigin("*")
    @RequestMapping(value = "/forWeek", method = RequestMethod.GET)
    public Map<String, Object> searchFeedForWeek(@RequestHeader(name="Authorization") String accessToken){
        Map<String, Object> response = new LinkedHashMap<>();
        TokenDTO.AccessTokenDTO token = userservice.getAccessToken(accessToken);

        String startDate = BaseTimeEntity.getWeekBeforeString();
        String endDate = BaseTimeEntity.getCurrentTimeString();
        System.out.println(startDate + " ~ " + endDate);

        ArrayList<FeedDTO.responseDTO> res = feedService.searchFeed(token.getFb_id(), token.getIg_id(),BaseTimeEntity.timeToString(startDate).toString(), BaseTimeEntity.timeToString(endDate).toString());
        response.put("feed_list", res);

        return response;
    }

    @CrossOrigin("*")
    @RequestMapping(value = "/forMonth", method = RequestMethod.GET)
    public Map<String, Object> searchFeedForMonth(@RequestHeader(name="Authorization") String accessToken){
        Map<String, Object> response = new LinkedHashMap<>();
        TokenDTO.AccessTokenDTO token = userservice.getAccessToken(accessToken);

        String startDate = BaseTimeEntity.getMonthBeforeString();
        String endDate = BaseTimeEntity.getCurrentTimeString();
        System.out.println(startDate + " ~ " + endDate);

        ArrayList<FeedDTO.responseDTO> res = feedService.searchFeed(token.getFb_id(), token.getIg_id(),BaseTimeEntity.timeToString(startDate).toString(), BaseTimeEntity.timeToString(endDate).toString());
        response.put("feed_list", res);

        return response;
    }

    @CrossOrigin("*")
    @RequestMapping(value = "/getFeed/{post_id}", method = RequestMethod.GET)
    public Map<String, Object> getOneFeed(@PathVariable String post_id, @RequestHeader(name="Authorization") String accessToken){
        Map<String, Object> response = new LinkedHashMap<>();
        TokenDTO.AccessTokenDTO token = userservice.getAccessToken(accessToken);
        Optional<FeedDTO.responseDTO> res = feedService.searchOneFeed(post_id);
        if(!res.isPresent()){
            throw new IllegalArgumentException();
        }
        response.put("feed", res);

        return response;
    }

    @CrossOrigin("*")
    @RequestMapping(value = "/searchKeyword/{keyword}", method = RequestMethod.GET)
    public Map<String, Object> searchByKeyword(@PathVariable String keyword, @RequestHeader(name="Authorization") String accessToken){
        Map<String, Object> response = new LinkedHashMap<>();
        TokenDTO.AccessTokenDTO token = userservice.getAccessToken(accessToken);
        ArrayList<FeedDTO.responseDTO> res = feedService.searchFeedByKeyword(token.getFb_id(),token.getIg_id(),keyword);
        if(res.size() == 0)
            response.put("feed", "no match");
        else
            response.put("feed", res);

        return response;
    }

}
