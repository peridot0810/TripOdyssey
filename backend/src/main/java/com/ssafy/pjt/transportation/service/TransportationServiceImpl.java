package com.ssafy.pjt.transportation.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.pjt.common.dto.request.UserGroupRequestDto;
import com.ssafy.pjt.common.entity.MemberRole;
import com.ssafy.pjt.common.exception.FileUploadIllegalArgumentException;
import com.ssafy.pjt.common.exception.UnauthorizedRoleAccessException;
import com.ssafy.pjt.common.exception.UserNotInGroupException;
import com.ssafy.pjt.common.service.UserValidationService;
import com.ssafy.pjt.transportation.dto.request.AddTransportationRequestDto;
import com.ssafy.pjt.transportation.dto.request.UpdateTransportationRequestDto;
import com.ssafy.pjt.transportation.dto.response.TransportationResponseDto;
import com.ssafy.pjt.transportation.repository.TransportationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransportationServiceImpl implements TransportationService{
	
	private final TransportationRepository transportationRepository;
	private final UserValidationService userValidationService;
	
//	private final List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp");
	private final List<String> allowedExtensions = Arrays.asList(".png");
	
	@Value("${spring.servlet.multipart.location}")
	private String IMG_UPLOAD_DIR;
	
	@Value("${transportation.img.get-url-prefix}")
	private String PROFILE_IMG_PATH_PREFIX;

	@Override
	public Integer addTransportation(String userId, Integer groupId, AddTransportationRequestDto addRequest) {
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		// 유저 역할 확인
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.LOGISTICS.getId())) {
			throw new UnauthorizedRoleAccessException("'교통' 담당자만 교통 정보를 추가할 수 있습니다.");
		}
		
		// 비즈니스 로직 
		addRequest.setGroupId(groupId);
		transportationRepository.addTransportation(addRequest);
		return addRequest.getTransportationId();
	}
	
	@Override
	public void deleteTransportation(String userId, Integer groupId, Integer transportationId) {
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		// 유저 역할 확인
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.LOGISTICS.getId())) {
			throw new UnauthorizedRoleAccessException("'교통' 담당자만 교통 정보를 삭제할 수 있습니다.");
		}
		
		// 비즈니스 로직 
		transportationRepository.deleteTransportation(transportationId);
	}
	
	
	
	@Override
	public void updateTransportation(String userId, Integer groupId, UpdateTransportationRequestDto updateRequest) {
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		// 유저 역할 확인
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.LOGISTICS.getId())) {
			throw new UnauthorizedRoleAccessException("'교통' 담당자만 교통 정보를 수정할 수 있습니다.");
		}
		
		// 비즈니스 로직 
		transportationRepository.updateTransportation(updateRequest);
	}
	
	
	@Override
	public TransportationResponseDto getTransportation(String userId, Integer groupId, Integer transportationId) {
		// 요청한 유저가 그룹원이 맞는지 확인 
		if(!transportationRepository.checkUserInGroup(new UserGroupRequestDto(userId, groupId))) {
			// 아니라면 예외 던지기 
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		
		// 비즈니스 로직 
		return transportationRepository.getTransportation(transportationId);
	}
	
	@Override
	public List<TransportationResponseDto> getTransportationList(String userId, Integer groupId) {
		// 요청한 유저가 그룹원이 맞는지 확인 
		if(!transportationRepository.checkUserInGroup(new UserGroupRequestDto(userId, groupId))) {
			// 아니라면 예외 던지기 
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		
		// 비즈니스 로직
		return transportationRepository.getTransportationList(groupId);
	}
	
	@Override
	public Path handleTransportationImageUpload(String userId, Integer groupId, MultipartFile file) throws IOException {
		
		// 그룹원인지 확인 
		if(!userValidationService.isUserInGroup(userId, groupId)) {
			throw new UserNotInGroupException("속하지 않은 그룹의 정보를 요청하였습니다.");
		}
		// 유저 역할 확인
		if(!userValidationService.isUserRoleValid(userId, groupId, MemberRole.LOGISTICS.getId())) {
			throw new UnauthorizedRoleAccessException("'교통' 담당자만 요청할 수 있는 기능입니다.");
		}
		
		
		// 저장 경로 설정
		File dir = new File(IMG_UPLOAD_DIR+PROFILE_IMG_PATH_PREFIX);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		// 파일 이름 생성 및 검증 
		String originalFileName = file.getOriginalFilename();
		if (originalFileName == null || originalFileName.trim().isEmpty()) {
	        throw new FileUploadIllegalArgumentException("파일 이름이 유효하지 않습니다.");
	    }
		
		// 파일 확장자 추출 및 검증 
		String extension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
		if (!allowedExtensions.contains(extension)) {
	        throw new FileUploadIllegalArgumentException("허용되지 않는 파일 확장자입니다: " + extension);
	    }
		
		// 저장 파일명 생성
	    String baseName = originalFileName.substring(0, originalFileName.lastIndexOf(".")).replaceAll("[^a-zA-Z0-9]", "_");
	    String savedFileName = UUID.randomUUID() + "_" + baseName + extension;
		
		log.debug("original file name : {}", originalFileName);
		log.debug("extension : {}", extension);
		log.debug("savedFileName : {}", savedFileName);
		
		// 실제 파일 저장
		Path filePath = Paths.get(IMG_UPLOAD_DIR+PROFILE_IMG_PATH_PREFIX, savedFileName);
		file.transferTo(filePath.toFile());
		
		// 로깅 
		log.debug("Uploaded file: {}, Saved as: {}", originalFileName, savedFileName);
		
		return filePath;
	}
}
