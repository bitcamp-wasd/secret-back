import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { IsEnum } from 'class-validator';
import { now, Types } from 'mongoose';
import { ChallengeList } from 'src/challengeList/challengeList.collection';
import { State } from './state.enum';
import * as mongoosePaginate from 'mongoose-paginate-v2';

@Schema({ timestamps: true, collection: 'challenge' })
export class Challenge {
  @Prop()
  title: string;

  @Prop()
  numberOfPeople: number;

  @Prop({ default: now() })
  createDate: Date;

  @Prop()
  endDate: Date;

  @Prop()
  voteEndDate: Date;

  @Prop()
  count: number;

  @Prop({ type: String, enum: State, default: State.RECRUITMENT })
  @IsEnum(State)
  state: State;

  @Prop({ default: [], type: Types.ObjectId, ref: 'ChallengeList' })
  challengeList: ChallengeList[];
}

export const ChallengeSchema = SchemaFactory.createForClass(Challenge);
ChallengeSchema.plugin(mongoosePaginate);
