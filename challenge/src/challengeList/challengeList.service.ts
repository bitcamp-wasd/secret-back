import { Injectable, Logger, NotAcceptableException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { S3Service } from 'src/aws/s3.service';
import { ChallengeList } from './challengeList.collection';
import { Model } from 'mongoose';
import { UserAuthDto } from 'src/auth/userAuth.dto';
import { challengeUploadDto } from './dto/request/challenge-upload.dto';
import * as crypto from 'crypto';
import { ChallengeUploadResponseDto } from './dto/response/challenge-upload-res.dto';
import { ChallengeService } from 'src/challenge/challenge.service';
import { VoteRequestDto } from 'src/vote/dto/request/voteRequest.dto';
import { VoteService } from 'src/vote/vote.service';
import { State } from 'src/challenge/state.enum';

@Injectable()
export class ChallengeListService {
  private readonly logger: Logger = new Logger(ChallengeService.name);
  constructor(
    private readonly s3Service: S3Service,
    @InjectModel(ChallengeList.name)
    private challengeListModel: Model<ChallengeList>,
    private readonly challengeService: ChallengeService,
    private readonly voteService: VoteService,
  ) {}

  /**
   * 챌린지 동영상 업로드하기
   * @param userAuth
   * @param challenge
   * @returns
   */
  async uploadChallenge(
    userAuth: UserAuthDto,
    challengeUploadDto: challengeUploadDto,
  ) {
    const challenge = await this.challengeService.getChallenge(
      challengeUploadDto.challengeId,
    );

    try {
      if (challenge.state !== State.RECRUITMENT)
        throw new NotAcceptableException('이미 마감된 챌린지입니다.');
      // 참여 중복 검사
      // await this.challengeService.chekcUserDuplicate(
      //   userAuth,
      //   challengeUploadDto.challengeId,
      // );
    } catch (error) {
      throw error;
    }
    const uuid = crypto.randomUUID();

    const { videoPresignedUrl, videoFilename } =
      await this.s3Service.uploadVideo(uuid);
    const { thumbnailPresignedUrl, thumbnailFilename } =
      await this.s3Service.uploadThumbnail(challengeUploadDto.thumbnail, uuid);

    const newChallenge: ChallengeList = new this.challengeListModel({
      videoPath: videoFilename,
      thumbnailPath: thumbnailFilename,
      userId: userAuth.userId,
      cnt: 0,
    });

    const saveChallenge = await this.challengeListModel.create(newChallenge);

    await this.challengeService.addChallengeList(
      challengeUploadDto.challengeId,
      saveChallenge,
    );

    return new ChallengeUploadResponseDto(
      videoPresignedUrl,
      thumbnailPresignedUrl,
    );
  }

  /**
   * 챌린지 영상 투표하기
   * @param userAuth
   * @param voteRequestDto
   */
  async voteChallengeList(
    userAuth: UserAuthDto,
    voteRequestDto: VoteRequestDto,
  ) {
    try {
      await this.voteService.vote(userAuth, voteRequestDto);
      await this.challengeService.incrementVoteCnt(voteRequestDto);
    } catch (error) {
      throw error;
    }
  }
}
