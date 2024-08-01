import { IsNumber, IsString } from 'class-validator';

export class UserAuthDto {
  @IsNumber()
  userId: number;
  @IsString()
  role: string;
  @IsString()
  nickName: string;

  isAdmin(): boolean {
    return this.role === 'ROLE_ADMIN';
  }
}
