import { ForbiddenException, Injectable, Logger } from '@nestjs/common';
import { Challenge } from './challenge.collection';
import { InjectModel } from '@nestjs/mongoose';
import { Model, Types, PaginateModel } from 'mongoose';
import { UserAuthDto } from 'src/auth/userAuth.dto';
import { NewChallengeRequestDto } from './dto/request/new-challenge.dto';
import { VoteRequestDto } from 'src/vote/dto/request/voteRequest.dto';
import { State } from './state.enum';

@Injectable()
export class ChallengeService {
  private readonly logger: Logger = new Logger(ChallengeService.name);

  constructor(
    @InjectModel(Challenge.name) private challengeModel: Model<Challenge>,
    @InjectModel(Challenge.name)
    private paginateChallengeModel: PaginateModel<Challenge>,
  ) {}

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

  /**
   * 투표 수 하나 늘리기
   * @param voteRequestDto
   * @returns
   */
  async incrementVoteCnt(voteRequestDto: VoteRequestDto) {
    return this.challengeModel
      .findOneAndUpdate(
        {
          _id: voteRequestDto.challengeId,
          'challengeList._id': voteRequestDto.challengeListId,
        },
        { $inc: { 'challengeList.$.cnt': 1 } },
        { new: true },
      )
      .exec();
  }

  async getChallenge(challengeId: string) {
    return this.challengeModel.findById(challengeId);
  }

  async getChallengeAndChallengeVideo(challengeId: string) {
    return await this.challengeModel
      .findById(challengeId)
      .populate('challengeList')
      .exec();
  }
}
