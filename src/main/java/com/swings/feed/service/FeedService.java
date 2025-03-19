package com.swings.feed.service;

import com.swings.feed.entity.FeedEntity;
import com.swings.feed.repository.FeedRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedService {

    private final FeedRepository feedRepository;

    public FeedService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    // 피드 작성
    public FeedEntity createFeed(FeedEntity feed) {
        return feedRepository.save(feed);
    }

    // 전체 피드 조회
    public List<FeedEntity> getAllFeeds() {
        return feedRepository.findAll();
    }

    // 특정 피드 조회
    @Transactional
    public Optional<FeedEntity> getFeedById(Long feedId) {
        return feedRepository.findById(feedId);
    }

    // 피드 수정
    public FeedEntity updateFeed(Long feedId, FeedEntity updatedFeed) {
        return feedRepository.findById(feedId)
                .map(feed -> {
                    feed.setCaption(updatedFeed.getCaption());
                    feed.setImageUrl(updatedFeed.getImageUrl());
                    // user나 createdAt 등 변경 로직 필요 시 추가
                    return feedRepository.save(feed);
                })
                .orElseThrow(() -> new RuntimeException("Feed not found with id: " + feedId));
    }

    // 피드 삭제
    public void deleteFeed(Long feedId) {
        feedRepository.deleteById(feedId);
    }

    // 좋아요 증가
    public FeedEntity likeFeed(Long feedId) {
        FeedEntity feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("Feed not found with id: " + feedId));

        feed.setLikes(feed.getLikes() + 1); // 좋아요 증가
        return feedRepository.save(feed);
    }

    // 좋아요 감소
    public FeedEntity unlikeFeed(Long feedId) {
        FeedEntity feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("Feed not found with id: " + feedId));

        if (feed.getLikes() > 0) {
            feed.setLikes(feed.getLikes() - 1); // 좋아요 감소
        }
        return feedRepository.save(feed);
    }

}
