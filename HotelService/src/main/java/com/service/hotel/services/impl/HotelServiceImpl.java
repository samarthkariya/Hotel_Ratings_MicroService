package com.service.hotel.services.impl;

import com.service.hotel.entity.Hotel;
import com.service.hotel.exception.ResourceNotFoundException;
import com.service.hotel.repository.HotelRepository;
import com.service.hotel.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;
    @Override
    public Hotel createHotel(Hotel hotel) {
        String id = UUID.randomUUID().toString();
        hotel.setId(id);
        Hotel hotel1 =hotelRepository.save(hotel);
        return hotel1;
    }

    @Override
    public List<Hotel> getAllHotels() {

        return hotelRepository.findAll();
    }

    @Override
    public Hotel getHotel(String hotelId) {
        return hotelRepository.findById(hotelId).orElseThrow(()->new ResourceNotFoundException("Hotel with given id is not found !!: "+hotelId));
    }
}
