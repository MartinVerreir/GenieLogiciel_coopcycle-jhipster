import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Panier from './panier';
import PanierDetail from './panier-detail';
import PanierUpdate from './panier-update';
import PanierDeleteDialog from './panier-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PanierUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PanierUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PanierDetail} />
      <ErrorBoundaryRoute path={match.url} component={Panier} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PanierDeleteDialog} />
  </>
);

export default Routes;
