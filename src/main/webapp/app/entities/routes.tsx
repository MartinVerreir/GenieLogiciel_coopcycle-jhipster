import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Client from './client';
import Panier from './panier';
import Paiement from './paiement';
import Livreur from './livreur';
import Cooperative from './cooperative';
import Commercant from './commercant';
import Commande from './commande';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}client`} component={Client} />
        <ErrorBoundaryRoute path={`${match.url}panier`} component={Panier} />
        <ErrorBoundaryRoute path={`${match.url}paiement`} component={Paiement} />
        <ErrorBoundaryRoute path={`${match.url}livreur`} component={Livreur} />
        <ErrorBoundaryRoute path={`${match.url}cooperative`} component={Cooperative} />
        <ErrorBoundaryRoute path={`${match.url}commercant`} component={Commercant} />
        <ErrorBoundaryRoute path={`${match.url}commande`} component={Commande} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
