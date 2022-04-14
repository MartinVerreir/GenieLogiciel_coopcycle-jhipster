import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPaiement } from 'app/shared/model/paiement.model';
import { getEntities as getPaiements } from 'app/entities/paiement/paiement.reducer';
import { IClient } from 'app/shared/model/client.model';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { ICommercant } from 'app/shared/model/commercant.model';
import { getEntities as getCommercants } from 'app/entities/commercant/commercant.reducer';
import { IPanier } from 'app/shared/model/panier.model';
import { getEntity, updateEntity, createEntity, reset } from './panier.reducer';

export const PanierUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const paiements = useAppSelector(state => state.paiement.entities);
  const clients = useAppSelector(state => state.client.entities);
  const commercants = useAppSelector(state => state.commercant.entities);
  const panierEntity = useAppSelector(state => state.panier.entity);
  const loading = useAppSelector(state => state.panier.loading);
  const updating = useAppSelector(state => state.panier.updating);
  const updateSuccess = useAppSelector(state => state.panier.updateSuccess);
  const handleClose = () => {
    props.history.push('/panier');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getPaiements({}));
    dispatch(getClients({}));
    dispatch(getCommercants({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...panierEntity,
      ...values,
      paiement: paiements.find(it => it.id.toString() === values.paiement.toString()),
      client: clients.find(it => it.id.toString() === values.client.toString()),
      commercant: commercants.find(it => it.id.toString() === values.commercant.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...panierEntity,
          paiement: panierEntity?.paiement?.id,
          client: panierEntity?.client?.id,
          commercant: panierEntity?.commercant?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopcycleApp.panier.home.createOrEditLabel" data-cy="PanierCreateUpdateHeading">
            <Translate contentKey="coopcycleApp.panier.home.createOrEditLabel">Create or edit a Panier</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="panier-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('coopcycleApp.panier.montant')}
                id="panier-montant"
                name="montant"
                data-cy="montant"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.panier.dateLimite')}
                id="panier-dateLimite"
                name="dateLimite"
                data-cy="dateLimite"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="panier-paiement"
                name="paiement"
                data-cy="paiement"
                label={translate('coopcycleApp.panier.paiement')}
                type="select"
              >
                <option value="" key="0" />
                {paiements
                  ? paiements.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="panier-client"
                name="client"
                data-cy="client"
                label={translate('coopcycleApp.panier.client')}
                type="select"
              >
                <option value="" key="0" />
                {clients
                  ? clients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="panier-commercant"
                name="commercant"
                data-cy="commercant"
                label={translate('coopcycleApp.panier.commercant')}
                type="select"
              >
                <option value="" key="0" />
                {commercants
                  ? commercants.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/panier" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PanierUpdate;
