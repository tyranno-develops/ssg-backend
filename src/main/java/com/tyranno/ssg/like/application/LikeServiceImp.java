package com.tyranno.ssg.like.application;

import com.tyranno.ssg.global.GlobalException;
import com.tyranno.ssg.global.ResponseStatus;
import com.tyranno.ssg.like.domain.Like;
import com.tyranno.ssg.like.dto.LikeListDto;
import com.tyranno.ssg.like.infrastructure.LikeRepository;
import com.tyranno.ssg.like.infrastructure.LikeRepositoryImp;
import com.tyranno.ssg.product.domain.Product;
import com.tyranno.ssg.product.dto.ProductIdListDto;
import com.tyranno.ssg.product.infrastructure.ProductRepository;
import com.tyranno.ssg.review.dto.ReviewIdListDto;
import com.tyranno.ssg.users.domain.Users;
import com.tyranno.ssg.users.infrastructure.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class LikeServiceImp implements LikeService{
    private final LikeRepository likeRepository;
    private final UsersRepository usersRepository;
    private final ProductRepository productRepository;
    private final LikeRepositoryImp likeRepositoryImp;

    @Override
    @Transactional
    public boolean modifyLike(Long productId, String uuid) {
        Users users =  usersRepository.findByUuid(uuid)
                .orElseThrow(() -> new GlobalException(ResponseStatus.NO_EXIST_USERS));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new GlobalException(ResponseStatus.NO_EXIST_PRODUCT));
        Optional<Like> like = likeRepository.findByProductIdAndUsersId(productId, users.getId()); // 상품을 좋아요 했는지 확인
        if (like.isPresent()) {// 이미 좋아요한 경우 삭제
            likeRepository.deleteByProductIdAndUsersId(productId, users.getId());
            return false;
        } else {// 좋아요 하지 않은 경우 추가
            Like newLike = Like.builder()
                    .product(product)
                    .users(users)
                            .build();
            likeRepository.save(newLike);
            return true; // 찜이 추가되었음을 나타냄
        }
    }

    @Override
    public int getLike(Long productId, String uuid) {
        Users users =  usersRepository.findByUuid(uuid)
                .orElseThrow(() -> new GlobalException(ResponseStatus.NO_EXIST_USERS));
        Optional<Like> like = likeRepository.findByProductIdAndUsersId(productId, users.getId());
        if (like.isPresent()) {
            return 11;
        } else {
            return 99;
        }
    }

    @Override
    public LikeListDto getLikeList(String uuid, Integer page) {
        final int PAGE_SIZE = 10;
        Users users = usersRepository.findByUuid(uuid)
                .orElseThrow(() -> new GlobalException(ResponseStatus.NO_EXIST_USERS));
        List<Long> likeIds = likeRepositoryImp.searchLikeIdsByUsersId(users.getId(), page);
        List<Map<String, Object>> likeIdList = new ArrayList<>();
        int startIndex = (page - 1) * PAGE_SIZE; // 페이지 번호가 1부터 시작하므로 수정
        for (int i = 0; i < likeIds.size(); i++) {
            Long likeId = likeIds.get(i);
            Map<String, Object> likeMap = new HashMap<>();
            likeMap.put("likeId", likeId);
            likeMap.put("idx", startIndex + i + 1); // 인덱스도 1부터 시작하도록 수정
            likeIdList.add(likeMap);
        }
        return LikeListDto.builder()
                .likeIds(likeIdList)
                .build();
    }
}
