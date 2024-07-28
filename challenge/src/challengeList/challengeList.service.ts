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
import { RestApiService } from 'src/restApi/api.service';
import { ChallengeVideo } from 'src/challenge/dto/response/challengeVideo.dto';

@Injectable()
export class ChallengeListService {
  private readonly logger: Logger = new Logger(ChallengeService.name);
  constructor(
    private readonly s3Service: S3Service,
    @InjectModel(ChallengeList.name)
    private challengeListModel: Model<ChallengeList>,
    private readonly challengeService: ChallengeService,
    private readonly voteService: VoteService,
    private readonly restApiService: RestApiService,
  ) {}

  async checkVote(userAuth: UserAuthDto, voteRequestDto: VoteRequestDto) {
    return await this.voteService.checkVote(userAuth, voteRequestDto);
  }

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
      this.logger.debug(challenge);
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
      title: challengeUploadDto.title,
      category: challengeUploadDto.category,
      description: challengeUploadDto.description,
      length: challengeUploadDto.length,
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
      await this.incrementVoteCnt(voteRequestDto);
    } catch (error) {
      throw error;
    }
  }

  /**
   * 투표 수 하나 늘리기
   * @param voteRequestDto
   * @returns
   */
  async incrementVoteCnt(voteRequestDto: VoteRequestDto) {
    return this.challengeListModel
      .findOneAndUpdate(
        {
          _id: voteRequestDto.challengeListId,
        },
        { $inc: { cnt: 1 } },
      )
      .exec();
  }

  async findByChallengeVideo(videoId: string) {
    const video = await this.challengeListModel.findById(videoId);
    const user = await this.restApiService.getUser([video.userId]);

    return new ChallengeVideo(
      video._id,
      video.videoPath,
      video.title,
      video.category,
      video.thumbnailPath,
      video.description,
      video.length,
      video.userId,
      video.cnt,
      user[0],
    );
  }
}
