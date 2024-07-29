import { ForbiddenException, Injectable, Logger } from '@nestjs/common';
import { Challenge } from './challenge.collection';
import { InjectModel } from '@nestjs/mongoose';
import { Model, Types, PaginateModel } from 'mongoose';
import { UserAuthDto } from 'src/auth/userAuth.dto';
import { NewChallengeRequestDto } from './dto/request/new-challenge.dto';
import { State } from './state.enum';
import { ChallengeBannerResponseDto } from './dto/response/challenge-data-res.dto';
import { CompleteChallengeDto } from './dto/response/challenge-complete-res.dto';
import { RestApiService } from 'src/restApi/api.service';
import { ChallengeVideo } from './dto/response/challengeVideo.dto';

@Injectable()
export class ChallengeService {
  private readonly logger: Logger = new Logger(ChallengeService.name);

  constructor(
    @InjectModel(Challenge.name) private challengeModel: Model<Challenge>,
    @InjectModel(Challenge.name)
    private paginateChallengeModel: PaginateModel<Challenge>,
    private readonly restApiService: RestApiService,
  ) {}

  async getCompleteList(pageNumber: number) {
    const completeList = await this.paginateChallengeModel.paginate(
      {
        state: State.COMPLETE,
      },
      {
        sort: { createdAt: -1 },
        limit: 16,
        page: pageNumber + 1,
      },
    );

    const response = completeList.docs.map(
      (challenge) => new CompleteChallengeDto(challenge._id, challenge.title),
    );
    return response;
  }

  async getBanner() {
    const challenge = await this.challengeModel.findOne({
      state: [State.RECRUITMENT, State.RECRUITMENT_COMPLETE, State.VOTE],
    });

    return new ChallengeBannerResponseDto(
      challenge._id,
      challenge.title,
      challenge.endDate,
      challenge.voteEndDate,
      challenge.state,
    );
  }

  /**
   * 투표 종료 및 정산
   */
  async endChallenge() {
    const challenges: any = await this.challengeModel
      .find({
        state: State.VOTE,
      })
      .populate('challengeList')
      .exec();

    console.log(challenges);

    challenges.forEach((challenge) => {
      if (challenge.voteEndDate >= new Date()) return;

      const challengeListItems: any = challenge.challengeList;
      console.log('--------------------');
      console.log(challengeListItems);
      const challengeList: any = challengeListItems.sort(
        (a, b) => b.cnt - a.cnt,
      );

      const ranking = new Map();
      let rank = 0;
      let count = 0;

      for (let i = 0; i < challengeList.length; i++) {
        if (!ranking.has(rank + 1)) ranking.set(rank + 1, []);

        if (i != 0 && challengeList[i].cnt === challengeList[i - 1].cnt) {
          console.log('rank: ' + rank);
          ranking.get(rank).push(challengeList[i]);
          count += 1;
        } else {
          rank += 1;
          if (count >= 3) break;
          ranking.get(rank).push(challengeList[i]);

          count += 1;
        }
      }
      // challenge.state = State.COMPLETE;
      // challenge.save();
      this.logger.debug(ranking);
      console.log(ranking);
    });
  }

  /**
   * 투표 시작 상태 변환
   */
  async startVoteChallenge() {
    const challenges = await this.challengeModel.find({
      state: [State.RECRUITMENT, State.RECRUITMENT_COMPLETE],
    });

    challenges.forEach((challenge) => {
      if (challenge.endDate >= new Date()) return;
      challenge.state = State.VOTE;
      challenge.save();
    });
  }

  /**
   * 새로운 챌린지 생성
   * @param data
   * @returns
   */
  async newChallenge(data: NewChallengeRequestDto): Promise<Types.ObjectId> {
    const newChallenge: Challenge = new this.challengeModel({
      title: data.title,
      numberOfPeople: data.numberOfPeople,
      endDate: data.endDate,
      voteEndDate: data.voteEndDate,
    });

    this.logger.debug(newChallenge);
    const { _id } = await this.challengeModel.create(newChallenge);

    this.logger.debug(_id);

    return _id;
  }

  /**
   * 챌린지 정보 가져오기
   * @param pageNumber
   * @returns
   */
  async challengeList(pageNumber: number) {
    this.logger.debug(
      await this.challengeModel.find({}).populate('challengeList'),
    );

    return this.paginateChallengeModel.paginate(
      {},
      {
        sort: { createdAt: -1 },
        limit: 16,
        page: pageNumber + 1,
        populate: 'challengeList',
      },
    );
  }

  /**
   * 챌린지 참여 동영상 추가
   * @param challengeId
   * @param saveChallenge
   * @returns
   */
  async addChallengeList(
    challengeId: string,
    saveChallenge,
  ): Promise<Types.ObjectId> {
    const challenge = await this.challengeModel.findByIdAndUpdate(
      challengeId,
      {
        $push: { challengeList: saveChallenge._id },
        $inc: { recruited: 1 },
      },
      { new: true },
    );

    if (challenge.recruited === challenge.numberOfPeople) {
      challenge.state = State.RECRUITMENT_COMPLETE;
      challenge.save();
    }

    return challenge._id;
  }

  /**
   * 챌린지 유저 중복 체크
   * @param userAuth
   * @param challengeId
   */
  async chekcUserDuplicate(userAuth: UserAuthDto, challengeId: string) {
    const challenge: any =
      await this.getChallengeAndChallengeVideo(challengeId);

    challenge.challengeList.forEach((challenge) => {
      if (challenge.userId === userAuth.userId)
        throw new ForbiddenException('이미 참여한 챌린지입니다.');
    });
  }

  async getChallenge(challengeId: string) {
    return this.challengeModel.findById(challengeId);
  }

  async getChallengeAndChallengeVideo(challengeId: string) {
    const challenge = await this.challengeModel
      .findById(challengeId)
      .populate('challengeList')
      .exec();

    const challengeList: any = challenge.challengeList;

    const userIds = challengeList.map((c) => c.userId);
    const users = await this.restApiService.getUser(userIds);

    const userMap = new Map<number, any>();

    users.forEach((user) => {
      const { userId, ...next } = user;
      userMap.set(userId, next);
    });

    const list = challenge.challengeList.map((challengeList: any) => {
      try {
        const data = new ChallengeVideo(
          challengeList._id,
          challengeList.videoPath,
          challengeList.title,
          challengeList.category,
          challengeList.thumbnailPath,
          challengeList.description,
          challengeList.length,
          challengeList.userId,
          challengeList.cnt,
          userMap.get(challengeList.userId),
        );

        return data;
      } catch (error) {
        console.log(error);
      }
    });

    return list;
  }

  async checkState(challengeId: string, state: State, errorMessage) {
    const challenge = await this.challengeModel.findById(challengeId);

    if (challenge.state !== state) throw errorMessage;
  }
}
