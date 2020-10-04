export interface ICardSettings {
  previewWidth: number;
  previewHeight: number;
  fallbackImage: string;
}

export class CardSettings implements ICardSettings {
  static DEFAULT: CardSettings = new CardSettings(96, 96, '/content/images/jhipster_family_member_1.svg');

  constructor(public previewWidth: number, public previewHeight: number, public fallbackImage: string) {}
}
