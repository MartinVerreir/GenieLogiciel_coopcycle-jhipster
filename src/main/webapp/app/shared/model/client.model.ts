import { IPanier } from 'app/shared/model/panier.model';
import { ICommande } from 'app/shared/model/commande.model';

export interface IClient {
  id?: number;
  nom?: string;
  prenom?: string;
  mail?: string | null;
  tel?: string | null;
  addresse?: string;
  paniers?: IPanier[] | null;
  commandes?: ICommande[] | null;
}

export const defaultValue: Readonly<IClient> = {};
