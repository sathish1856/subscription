package com.interview.subscription.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.subscription.dto.HotelDTO;
import com.interview.subscription.model.Hotel;
import com.interview.subscription.repository.HotelRepository;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("v1/hotels")
public class HotelController {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public List<HotelDTO> getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();
        return hotels.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable("id") Long id) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(id);
        return optionalHotel.map(hotel -> ResponseEntity.ok(convertToDTO(hotel)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public HotelDTO createHotel(@RequestBody HotelDTO hotelDTO) {
        Hotel hotel = convertToEntity(hotelDTO);
        Hotel savedHotel = hotelRepository.save(hotel);
        return convertToDTO(savedHotel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelDTO> updateHotel(@PathVariable("id") Long id, @RequestBody HotelDTO hotelDTO) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(id);
        return optionalHotel.map(hotel -> {
            updateHotelFromDTO(hotel, hotelDTO);
            Hotel updatedHotel = hotelRepository.save(hotel);
            return ResponseEntity.ok(convertToDTO(updatedHotel));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        return hotelRepository.findById(id)
                .map(hotel -> {
                    hotelRepository.delete(hotel);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private HotelDTO convertToDTO(Hotel hotel) {
        HotelDTO hotelDTO = new HotelDTO();
        BeanUtils.copyProperties(hotel, hotelDTO);
        hotelDTO.setHotelID(hotel.getHotelid());
        return hotelDTO;
    }

    private Hotel convertToEntity(HotelDTO hotelDTO) {
        Hotel hotel = new Hotel();
        BeanUtils.copyProperties(hotelDTO, hotel);
        hotel.setHotelid(hotelDTO.getHotelID());
        return hotel;
    }

    private void updateHotelFromDTO(Hotel hotel, HotelDTO hotelDTO) {
        try {
			objectMapper.updateValue(hotelDTO, hotel);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		}
    }
}
