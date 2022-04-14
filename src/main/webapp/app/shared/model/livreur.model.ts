import { ICommande } from 'app/shared/model/commande.model';
import { ICooperative } from 'app/shared/model/cooperative.model';

export interface ILivreur {
  id?: number;
  nom?: string;
  prenom?: string;
  tel?: string | null;
  commandes?: ICommande[] | null;
  cooperative?: ICooperative | null;
}

export const defaultValue: Readonly<ILivreur> = {};
