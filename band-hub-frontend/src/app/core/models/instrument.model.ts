export enum InstrumentEnum {
  ACOUSTIC_GUITAR = "Gitara akustyczna",
  ELECTRIC_GUITAR = "Gitara elektrycza",
  BASS = "Bas",
  DRUMS = "Perkusja",
  KEYBOARD = "Keyboard",
  VOICE = "Wokal"
}

export interface Instrument{
  id: number,
  name: string
}
