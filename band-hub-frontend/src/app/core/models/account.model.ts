export interface AccountRegisterRequest {
  firstName: string,
  lastName: string,
  birthDate: string,
  gender: boolean,
  email: string,
  password: string,
  city: string,
  country: string,
  roles: [{name: string}],
}

export interface AccountResponse {
  id: number,
  firstName: string,
  lastName: string,
  birthDate: Date,
  gender: boolean
  city: string,
  country: string,
  imageURL?: string
}

export interface AccountUpdateRequest {
  id: number,
  firstName: string,
  lastName: string,
  birthDate: string,
  gender: boolean
  city: string,
  country: string
}

export interface AccountPersonalData {
  firstName: string,
  lastName: string,
  day: number | null,
  month: number | null,
  year: number | null,
  gender: boolean | null,
  city: string,
  country: string
}

export interface AccountCredentials {
  email: string,
  password: string
}
