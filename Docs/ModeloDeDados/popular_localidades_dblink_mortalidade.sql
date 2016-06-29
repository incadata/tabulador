insert into regiao (id_regiao, nm_regiao, sg_regiao)
Select id_regiao, nm_regiao, sg_regiao
from (select * from dblink('dbname=mortalidade user=''conprev'' host=''srvdes02'' password=''inca2010''', 'select id_regiao, nm_regiao, sg_regiao From regiao') as
 (id_regiao bigint,
  nm_regiao character(15),
  sg_regiao character(6)
  )) a

insert into estado (id_estado, nm_estado, sg_estado, id_regiao)
Select id_estado, nm_estado, sg_estado, id_regiao
from (select * from dblink('dbname=mortalidade user=''conprev'' host=''srvdes02'' password=''inca2010''', 'select id_estado, nm_estado, sg_estado, id_regiao From estado') as
 (id_estado bigint,
  nm_estado character(256),
  sg_estado character(2),
  id_regiao bigint
  )) a


insert into regional_saude (id_regional_saude, nm_regional_saude, id_estado)
Select id_regional_saude, nm_regional_saude, id_estado
from (select * from dblink('dbname=mortalidade user=''conprev'' host=''srvdes02'' password=''inca2010''', 'select id_regional_saude, nm_regional_saude, id_estado From regional_saude Where fg_ignorado = false') as
 (id_regional_saude bigint,
  nm_regional_saude character(256),
  id_estado bigint
  )) a;

insert into cidade (id_cidade, nm_cidade, fg_capital_estado, id_regional_saude, id_estado)
Select id_cidade, nm_cidade, fg_capital_estado, id_regional_saude, id_estado
from (select * from dblink('dbname=mortalidade user=''conprev'' host=''srvdes02'' password=''inca2010''', 'select id_cidade, nm_cidade, id_estado, fg_capital_estado, id_regional_saude From cidade Where fg_ignorado = false') as
 (id_cidade bigint,
  nm_cidade character(256),
  id_estado bigint,
  fg_capital_estado boolean,
  id_regional_saude bigint
  )) a;