package com.tyranno.ssg.payment.presentation;

import com.tyranno.ssg.global.GlobalException;
import com.tyranno.ssg.global.ResponseEntity;
import com.tyranno.ssg.global.ResponseStatus;
import com.tyranno.ssg.payment.application.PaymentServiceImp;
import com.tyranno.ssg.payment.dto.ReadyRequestDto;
import com.tyranno.ssg.payment.dto.ReadyResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "결제", description = "Pay API")
@RequestMapping("/api/v1/pay")
@Slf4j
public class PaymentController {

    private final PaymentServiceImp paymentService;

    @Operation(summary = "카카오페이 결제 시작", description = "카카오페이 결제를 시작한다.")
    @PostMapping("/ready")
    public ResponseEntity<ReadyResponseDto> readyToKakaoPay(@RequestBody ReadyRequestDto readyRequestDto) {

        return new ResponseEntity<>(paymentService.kakaoPayReady(readyRequestDto));
    }

    /**
     * 결제 성공
     */
    @Operation(summary = "카카오페이 결제 성공", description = "카카오페이 결제 성공 시 redirect url")
    @GetMapping("/success/{id}")
    public ResponseEntity<?> successKakaoPay() {
        return new ResponseEntity<>("카카오페이 결제 성공");
    }
    @Operation(summary = "카카오페이 승인 요청", description = "카카오페이 결제 승인을 요청한다.")
    @GetMapping("/approve/{id}")
    public ResponseEntity<?> approveKakaoPay(@PathVariable String id, @RequestParam("pg_token") String pgToken) {
        return new ResponseEntity<>(paymentService.getKakaoPayApprove(id, pgToken));
    }

//    @Operation(summary = "카카오페이 승인 요청", description = "카카오페이 결제 승인을 요청한다.")
//    @PostMapping("/approve")
//    public ResponseEntity<?> approveKakaoPay(@RequestBody ApproveRequestDto approveRequestDto) {
//
//
//    }

    /**
     * 결제 진행 중 취소
     */
    @GetMapping("/cancel")
    public void cancel() {
        throw new GlobalException(ResponseStatus.PAY_CANCEL);
    }

    /**
     * 결제 실패
     */
    @GetMapping("/fail")
    public void fail() {
        throw new GlobalException(ResponseStatus.PAY_FAILED);
    }
}
