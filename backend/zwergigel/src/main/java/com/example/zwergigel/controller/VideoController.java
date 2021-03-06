package com.example.zwergigel.controller;

import com.example.zwergigel.dto.VideoDto;
import com.example.zwergigel.entity.User;
import com.example.zwergigel.entity.Video;
import com.example.zwergigel.repository.VideoRepository;
import com.example.zwergigel.service.TokenService;
import com.example.zwergigel.util.YoutubeUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("video")
@Transactional
public class VideoController {

    private final TokenService tokenService;
    private final VideoRepository videoRepository;

    public VideoController(TokenService tokenService, VideoRepository videoRepository) {
        this.tokenService = tokenService;
        this.videoRepository = videoRepository;
    }

    @GetMapping("/{token}")
    public ResponseEntity<Set<VideoDto>> all(@PathVariable("token") UUID token) {
        return tokenService.verify(token)
            .map(this::getVideosBy)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    private Set<VideoDto> getVideosBy(User u) {
        return videoRepository.findByUser(u).stream()
            .map(v -> new VideoDto(v.getId(), v.getUrl(), v.getDuration(), v.getPaid(), v.getName(), v.getThumbnailUrl()))
            .collect(Collectors.toSet());
    }

    @PostMapping("/add/{token}")
    public ResponseEntity<Set<VideoDto>> add(@PathVariable("token") UUID token, @RequestBody Map<String, String> dto) {
        final String url = dto.get("url");
        final long paid = Long.parseLong(dto.get("paid"));
        Optional<User> mbUser = tokenService.verify(token);
        if (mbUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        final User user = mbUser.get();
        final Video video = videoRepository.findByUrlAndUser(url, user)
            .map(v -> v.addPaid(paid))
            .orElseGet(() -> new Video(url, paid, YoutubeUtil.getData(url), user));
        videoRepository.save(video);
        return ResponseEntity.ok(getVideosBy(user));
    }

    @PostMapping("/delete/{token}")
    public ResponseEntity<Set<VideoDto>> delete(@PathVariable("token") UUID token, @RequestParam("id") Long id) {
        Optional<User> mbUser = tokenService.verify(token);
        if (mbUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        final User user = mbUser.get();
        videoRepository.deleteByIdAndUser(id, user);
        return ResponseEntity.ok(getVideosBy(user));
    }

    @GetMapping("/delete-all/{token}")
    public ResponseEntity<Set<VideoDto>> deleteAll(@PathVariable("token") UUID token) {
        Optional<User> mbUser = tokenService.verify(token);
        if (mbUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        final User user = mbUser.get();
        videoRepository.deleteAllByUser(user);
        return ResponseEntity.ok(getVideosBy(user));
    }

    @PostMapping("/plus/{token}")
    public ResponseEntity<Set<VideoDto>> plus(@PathVariable("token") UUID token,
                                              @RequestParam("id") Long id, @RequestParam("paid") Long paid) {
        Optional<User> mbUser = tokenService.verify(token);
        if (mbUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        final User user = mbUser.get();
        Optional<Video> mbVideo = videoRepository.findByIdAndUser(id, user);
        if (mbVideo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        mbVideo.map(v -> v.addPaid(paid)).ifPresent(videoRepository::save);
        return ResponseEntity.ok(getVideosBy(user));
    }
}
