import { IsString } from 'class-validator';

export class UserAuthDto {
  @IsString()
  name: string;
  @IsString()
  role: string;
  @IsString()
  nickName: string;

  isAdmin(): boolean {
    return this.role === 'ROLE_ADMIN';
  }
}
