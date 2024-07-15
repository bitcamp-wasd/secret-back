import { Injectable, Logger } from '@nestjs/common';
import { Challenge } from './challenge.collection';
import { InjectModel } from '@nestjs/mongoose';
import { Model, Types } from 'mongoose';

@Injectable()
export class ChallengeService {
  private readonly logger: Logger = new Logger(ChallengeService.name);

  constructor(
    @InjectModel(Challenge.name) private challengeModel: Model<Challenge>,
  ) {}

  async newChallenge(
    data: ReadableStream<Uint8Array>,
  ): Promise<Types.ObjectId> {
    const newChallenge: Challenge = new this.challengeModel({
      title: data['title'],
      numberOfPeople: data['numberOfPeople'],
      endDate: data['endDate'],
      voteEndDate: data['voteEndDate'],
    });

    this.logger.debug(newChallenge);
    const { _id } = await this.challengeModel.create(newChallenge);

    console.log(_id.toString());

    return _id;
  }

  async challengeList() {}
}
