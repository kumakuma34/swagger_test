package snsservice.snsservice.service;

import TemplateClass.BaseTimeEntity;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.AsyncRestTemplate;
import org.json.simple.JSONObject;
import org.springframework.web.client.RestTemplate;
import org.json.simple.parser.JSONParser;
import snsservice.snsservice.DTO.FeedDTO;
import snsservice.snsservice.DTO.TokenDTO;
import snsservice.snsservice.Entity.Feed;
import snsservice.snsservice.Repository.FeedRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FeedService {
    public final FeedRepository feedRepository;
    public final UserService userService;

    public Feed toEntity(FeedDTO.PostDTO dto, String user_id,String sns_type){
        return Feed.builder()
                .userId(user_id)
                .snsType(sns_type)
                .postId(dto.getPostID())
                .postCreateTime(dto.getPostCreateTime())
                .postCreator(dto.getPostCreator())
                .postMessage(dto.getPostMessage())
                .postImage(dto.getPostImage())
                .build();
    }
    public ArrayList<String> getPostListFromFeed(String fb_id, String fb_token, String time, boolean updateNow) throws ParseException {
        ArrayList<String> postList = new ArrayList<>();

        try{
            String url = "https://graph.facebook.com/v13.0/";
            url += (fb_id) + "/feed?access_token=" + fb_token;
            AsyncRestTemplate restTemplate = new AsyncRestTemplate(); // 비동기 전달
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            JSONObject jsonObject = new JSONObject();

            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            //params.add("access_token", fb_token);

            HttpHeaders headers = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

            RestTemplate rt = new RestTemplate();

            ResponseEntity<String> response = rt.exchange(
                    url, //{요청할 서버 주소}
                    HttpMethod.GET, //{요청할 방식}
                    entity, // {요청할 때 보낼 데이터}
                    String.class);

            HttpStatus statusCode = response.getStatusCode();
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(response.getBody());
            JSONObject jsonObj = (JSONObject) obj;

            if(statusCode != HttpStatus.OK){
                throw new IllegalArgumentException(String.format("invalid request"));
            }

            JSONArray arr = (JSONArray)jsonObj.get("data");
            for(int i = 0 ; i<arr.size(); i++){
                JSONObject now = (JSONObject)arr.get(i);
                String post_create_time = (String)now.get("created_time");
                //if post is created before search time, it should be already in database
                if(!BaseTimeEntity.compareTime(post_create_time,time,updateNow)) break;

                String post_id  = (String)now.get("id");
                postList.add(post_id);
                System.out.println(post_id);
            }
            System.out.println(response.toString());
        } catch(Exception e){
            e.printStackTrace();
        }
        return postList;
    }

    public ArrayList<FeedDTO.PostDTO> getPostInfo(ArrayList<String> feedList, String fb_token){
        ArrayList<FeedDTO.PostDTO> postInfo = new ArrayList<>();
        try{
            for(int i = 0 ; i < feedList.size(); i++){
                String post_id = feedList.get(i);
                String url = "https://graph.facebook.com/v13.0/";
                url += (post_id + "?fields=id,message,created_time,type,object_id,from,full_picture&" + "access_token=" + fb_token);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);

                JSONObject jsonObject = new JSONObject();

                LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                //params.add("access_token", fb_token);

                HttpHeaders headers = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

                RestTemplate rt = new RestTemplate();

                ResponseEntity<String> response = rt.exchange(
                        url, //{요청할 서버 주소}
                        HttpMethod.GET, //{요청할 방식}
                        entity, // {요청할 때 보낼 데이터}
                        String.class);

                System.out.println(response.toString());

                HttpStatus statusCode = response.getStatusCode();
                JSONParser jsonParser = new JSONParser();
                Object obj = jsonParser.parse(response.getBody());
                JSONObject jsonObj = (JSONObject) obj;

                if(statusCode != HttpStatus.OK){
                    throw new IllegalArgumentException(String.format("invalid request"));
                }
                String postId = (String)jsonObj.get("id");
                String postMessage = (String)jsonObj.get("message");
                Timestamp postCreatedTime = BaseTimeEntity.Dateformatting((String)jsonObj.get("created_time"));
                JSONObject from = (JSONObject)jsonObj.get("from");
                String postCreator = (String)from.get("name");
                String postImage = (String)jsonObj.get("full_picture");

                FeedDTO.PostDTO postDTO =
                        FeedDTO.PostDTO.builder()
                                .postID(postId)
                                .postMessage(postMessage)
                                .postCreateTime(postCreatedTime)
                                .postCreator(postCreator)
                                .postImage(postImage)
                                .build();
                postInfo.add(postDTO);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return postInfo;
    }

    public ArrayList<FeedDTO.PostDTO> getIgFeed(String ig_token, String time, boolean updateNow){
        ArrayList<FeedDTO.PostDTO> postInfo = new ArrayList<>();
        try{
            String url = "https://graph.instagram.com/me/media?fields=id,username,caption,timestamp,media_url&";
            url += ("access_token=" + ig_token);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            JSONObject jsonObject = new JSONObject();

            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            //params.add("access_token", fb_token);

            HttpHeaders headers = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

            RestTemplate rt = new RestTemplate();

            ResponseEntity<String> response = rt.exchange(
                    url, //{요청할 서버 주소}
                    HttpMethod.GET, //{요청할 방식}
                    entity, // {요청할 때 보낼 데이터}
                    String.class);

            System.out.println(response.toString());

            HttpStatus statusCode = response.getStatusCode();
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(response.getBody());
            JSONObject jsonObj = (JSONObject) obj;

            if(statusCode != HttpStatus.OK){
                throw new IllegalArgumentException(String.format("invalid request"));
            }

            JSONArray arr = (JSONArray)jsonObj.get("data");
            for(int i = 0 ; i<arr.size(); i++){
                JSONObject now = (JSONObject)arr.get(i);
                Timestamp post_create_time = BaseTimeEntity.Dateformatting((String)now.get("timestamp"));
                System.out.println(post_create_time);
                //if post is created before search time, it should be already in database
                //if(!BaseTimeEntity.compareTime(post_create_time,time,updateNow)) break;
                FeedDTO.PostDTO feeddto = FeedDTO.PostDTO.builder()
                        .postID((String)now.get("id"))
                        .postCreator((String)now.get("username"))
                        .postMessage((String)now.get("caption"))
                        .postImage((String)now.get("media_url"))
                        .postCreateTime(post_create_time)
                        .build();
                postInfo.add(feeddto);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return postInfo;
    }

    public void addFeed(ArrayList<FeedDTO.PostDTO> postList, String userId , String snsType){
        for(int i = 0 ; i < postList.size(); i++){
            FeedDTO.PostDTO post = postList.get(i);
            System.out.println(post.getPostCreateTime());
            Feed entity = toEntity(post, userId,snsType);
            Optional<Feed> feed = feedRepository.findOneByPostId(post.getPostID());
            if(!feed.isPresent()){
                feedRepository.save(entity);
            }
        }
    }

    public ArrayList<FeedDTO.responseDTO> getAllFeed(String fb_id, String ig_id){
        ArrayList<FeedDTO.responseDTO> response = new ArrayList<>();

        Optional<List<Feed>> feed_list = feedRepository.findAllByUserAndSort(fb_id, ig_id);
        if(feed_list.isPresent()){
            List<Feed> list = feed_list.get();
            for(int i = 0 ; i < list.size(); i++){
                Feed now = list.get(i);
                FeedDTO.responseDTO dto =FeedDTO.responseDTO.builder()
                        .postId(now.getPostId())
                        .snsType(now.getSnsType())
                        .postCreator(now.getPostCreator())
                        .postMessage(now.getPostMessage())
                        .postImage(now.getPostImage())
                        .postCreateTime(now.getPostCreateTime().toString())
                        .build();
                response.add(dto);
            }
        }
        return response;
    }
    public ArrayList<FeedDTO.responseDTO> searchFeed(String fb_id, String ig_id, String start, String end){
        ArrayList<FeedDTO.responseDTO> response = new ArrayList<>();

        Optional<List<Feed>> feed_list = feedRepository.findAllByUserAndDateAndSort(fb_id, ig_id,start,end);
        if(feed_list.isPresent()){
            List<Feed> list = feed_list.get();
            for(int i = 0 ; i < list.size(); i++){
                Feed now = list.get(i);
                FeedDTO.responseDTO dto =FeedDTO.responseDTO.builder()
                        .postId(now.getPostId())
                        .snsType(now.getSnsType())
                        .postCreator(now.getPostCreator())
                        .postMessage(now.getPostMessage())
                        .postImage(now.getPostImage())
                        .postCreateTime(now.getPostCreateTime().toString())
                        .build();
                response.add(dto);
            }
        }
        return response;
    }

    public Optional<FeedDTO.responseDTO> searchOneFeed(String post_id){
        Optional<Feed> feed = feed = feedRepository.findOneByPostId(post_id);
        FeedDTO.responseDTO dto = new FeedDTO.responseDTO();
        if(feed.isPresent()){
            dto = FeedDTO.responseDTO.builder()
                    .postId(feed.get().getPostId())
                    .snsType(feed.get().getSnsType())
                    .postMessage(feed.get().getPostMessage())
                    .postCreator(feed.get().getPostCreator())
                    .postCreateTime(feed.get().getPostCreateTime().toString())
                    .postImage(feed.get().getPostImage())
                    .build();
        }
        Optional<FeedDTO.responseDTO> res = Optional.ofNullable(dto);
        return res;
    }

    public ArrayList<FeedDTO.responseDTO> searchFeedByKeyword(String fb_id, String ig_id, String keyword){
        ArrayList<FeedDTO.responseDTO> response = new ArrayList<>();

        Optional<List<Feed>> feed_list = feedRepository.findAllByUserAndKewwordSort(fb_id, ig_id,keyword);
        if(feed_list.isPresent()){
            List<Feed> list = feed_list.get();
            for(int i = 0 ; i < list.size(); i++){
                Feed now = list.get(i);
                FeedDTO.responseDTO dto =FeedDTO.responseDTO.builder()
                        .postId(now.getPostId())
                        .snsType(now.getSnsType())
                        .postCreator(now.getPostCreator())
                        .postMessage(now.getPostMessage())
                        .postImage(now.getPostImage())
                        .postCreateTime(now.getPostCreateTime().toString())
                        .build();
                response.add(dto);
            }
        }
        return response;
    }

    @Scheduled(fixedDelay = 60000)
    // @Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}") // 문자열 milliseconds 사용 시
    public void scheduleFixedDelayTask() throws InterruptedException {
        System.out.println("Scheduler Run");
        List<TokenDTO.checkDTO> new_list = new ArrayList<>();
        for(int i = 0 ; i < TokenService.checkJWTTokenList.size(); i++){
            TokenDTO.checkDTO dto = TokenService.checkJWTTokenList.get(i);
            TokenDTO.AccessTokenDTO token = userService.getAccessToken(dto.getJwt_token());
            String time = "1990-00-00-00-00";
            //String time = BaseTimeEntity.getCurrentTimeMinusMin();

            //facebook
            try {
                ArrayList<String> feedList = getPostListFromFeed(token.getFb_id(), token.getFb_access_token(), time, false);
                ArrayList<FeedDTO.PostDTO> postList = getPostInfo(feedList, token.getFb_access_token());
                addFeed(postList, token.getFb_id(), "facebook");

                //instagram
                ArrayList<FeedDTO.PostDTO> ig_postList = getIgFeed(token.getIg_access_token(), time, false);
                addFeed(ig_postList, token.getIg_id(), "instagram");
            }catch(Exception e){
                e.printStackTrace();
            }


            System.out.println(dto.toString() + time);
            if(dto.getCount() != 1)
                new_list.add(TokenDTO.checkDTO.builder().jwt_token(dto.getJwt_token()).count(dto.getCount()-1).build());
        }
        TokenService.checkJWTTokenList = new_list;
    }

}
