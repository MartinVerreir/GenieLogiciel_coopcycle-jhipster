import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './commercant.reducer';

export const CommercantDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const commercantEntity = useAppSelector(state => state.commercant.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="commercantDetailsHeading">
          <Translate contentKey="coopcycleApp.commercant.detail.title">Commercant</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{commercantEntity.id}</dd>
          <dt>
            <span id="carte">
              <Translate contentKey="coopcycleApp.commercant.carte">Carte</Translate>
            </span>
          </dt>
          <dd>{commercantEntity.carte}</dd>
          <dt>
            <span id="menus">
              <Translate contentKey="coopcycleApp.commercant.menus">Menus</Translate>
            </span>
          </dt>
          <dd>{commercantEntity.menus}</dd>
          <dt>
            <span id="horaires">
              <Translate contentKey="coopcycleApp.commercant.horaires">Horaires</Translate>
            </span>
          </dt>
          <dd>{commercantEntity.horaires}</dd>
          <dt>
            <span id="adresse">
              <Translate contentKey="coopcycleApp.commercant.adresse">Adresse</Translate>
            </span>
          </dt>
          <dd>{commercantEntity.adresse}</dd>
        </dl>
        <Button tag={Link} to="/commercant" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/commercant/${commercantEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CommercantDetail;
