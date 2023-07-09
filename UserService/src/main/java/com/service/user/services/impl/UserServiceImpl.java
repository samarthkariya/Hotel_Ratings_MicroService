package com.service.user.services.impl;

import com.service.user.entities.Hotel;
import com.service.user.entities.Rating;
import com.service.user.entities.User;
import com.service.user.exception.ResourceNotFoundException;
import com.service.user.external.service.HotelService;
import com.service.user.repositories.UserRepository;
import com.service.user.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;


    private Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {
        String id = UUID.randomUUID().toString();
        user.setUserId(id);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {

        return userRepository.findAll().stream().map(user -> {
            log.info("user info "+user.toString());
            Rating[] ratingsofuser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(), Rating[].class);
            log.info(ratingsofuser.toString());

            List<Rating> ratings = Arrays.stream(ratingsofuser).toList();
            log.info(ratings.toString());
            log.info(ratings.toString());
//        fetch hotel data using hotel ID
            List<Rating> ratingList = ratings.stream().map(rating -> {
                //http://localhost:8083/hotels/f1fe9ac8-d8f9-4837-9c93-e0706c8d9875
//            ResponseEntity<Hotel> forhotelEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
                Hotel hotel  =hotelService.getHotel(rating.getHotelId());
                log.info(hotel.getAbout());
                log.info("hotel id "+rating.getHotelId());
                rating.setHotel(hotel);

                return rating;
            }).collect(Collectors.toList());
            log.info("list of user rating "+ratingList.toString());
            user.setRating(ratingList);

            return user;
        }).collect(Collectors.toList());


    }

    @Override
    public User getUser(String userId) {
       //get user from database with help of user repository
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with given id is not found !!: "+userId));

        // fetch rating with user ID

        Rating[] ratingsofuser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+userId, Rating[].class);
        log.info(ratingsofuser.toString());

        List<Rating> ratings = Arrays.stream(ratingsofuser).toList();
//        
        log.info(ratings.toString());
//        fetch hotel data using hotel ID
        List<Rating> ratingList = ratings.stream().map(rating -> {
            //http://localhost:8083/hotels/f1fe9ac8-d8f9-4837-9c93-e0706c8d9875
//            ResponseEntity<Hotel> forhotelEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
            Hotel hotel  =hotelService.getHotel(rating.getHotelId());
            log.info(hotel.getAbout());
            rating.setHotel(hotel);

            return rating;
        }).collect(Collectors.toList());

        user.setRating(ratingList);

       return user;
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User updateUser(User user) {
        return null;
    }
}
